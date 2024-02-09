package com.cyecize.app.api.store.order;

import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartCouponCodeDto;
import com.cyecize.app.api.store.cart.ShoppingCartDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartService;
import com.cyecize.app.api.store.delivery.DeliveryAddress;
import com.cyecize.app.api.store.delivery.DeliveryAddressService;
import com.cyecize.app.api.store.order.dto.OrderDto;
import com.cyecize.app.api.store.order.dto.OrderItemDto;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.api.store.pricing.Price;
import com.cyecize.app.api.store.pricing.PricingService;
import com.cyecize.app.api.store.promotion.coupon.CouponCodeService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.UserService;
import com.cyecize.app.api.warehouse.WarehouseService;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.MathUtil;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DeliveryAddressService addressService;

    private final ShoppingCartService shoppingCartService;

    private final PricingService pricingService;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ModelMapper modelMapper;

    private final ProductService productService;

    private final UserService userService;

    private final MailService mailService;

    private final SpecificationExecutor specificationExecutor;

    private final WarehouseService warehouseService;

    private final CouponCodeService couponCodeService;

    @Override
    @Transactional
    public void createOrder(CreateOrderLoggedInDto dto, User currentUser) {
        if (!this.shoppingCartService.hasItems(dto.getSessionId())) {
            throw new ApiException(ValidationMessages.SHOPPING_CART_EMPTY);
        }

        final Long userId = currentUser != null ? currentUser.getId() : null;

        final DeliveryAddress address = this.addressService.createAddress(dto.getUserAddress());
        final Order order = this.createOrder(
                address,
                dto.getSessionId(),
                userId,
                dto.getUserAgreedPrice()
        );
        this.postOrderCreated(dto.getSessionId(), order, userId);
    }

    private void postOrderCreated(String cartSessionId, Order order, Long userId) {
        log.info("Order {} was created! Executing post order procedures.", order.getId());
        final List<String> emailsOfAdmins = this.userService.getEmailsOfAdmins();
        final OrderDto orderDto = this.getOrder(order.getId());

        try {
            log.info("Sending emails to admins '{}' and customer '{}' for order {}.",
                    emailsOfAdmins,
                    order.getAddress().getEmail(),
                    order.getId()
            );
            this.mailService.sendEmail(EmailTemplate.NEW_ORDER_ADMINS, orderDto, emailsOfAdmins);
            this.mailService.sendEmail(
                    EmailTemplate.NEW_ORDER_CUSTOMER,
                    orderDto,
                    List.of(order.getAddress().getEmail())
            );
            this.shoppingCartService.removeAllItems(cartSessionId);
        } catch (RuntimeException ex) {
            log.error("Order {} creation will be aborted due to post order procedure failure.",
                    order.getId(),
                    ex
            );
            throw ex;
        }
    }

    private Order createOrder(DeliveryAddress address,
            String cartSessionId,
            Long userId,
            Double userAgreedPrice) {
        final ShoppingCartDetailedDto cart = this.shoppingCartService
                .getShoppingCart(cartSessionId, false);

        final ShoppingCartCouponCodeDto couponCode = cart.getCouponCode();
        if (couponCode != null && !this.couponCodeService.useCouponCode(couponCode.getCode())) {
            this.shoppingCartService.removeCouponCode(cartSessionId);
            log.error("Invalid coupon code {} prevented order submission!", couponCode.getCode());
            throw new ApiException(ValidationMessages.COUPON_CODE_INVALID);
        }

        final Price price = this.pricingService.getPrice(cartSessionId);
        final Order order = new Order();
        order.setAddressId(address.getId());
        order.setAddress(address);

        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING);
        order.setUserId(userId);
        order.setDeliveryPrice(price.isFreeDelivery() ? 0D : price.getDeliveryPrice());
        order.setTotalDiscounts(price.getTotalDiscounts());
        if (couponCode != null) {
            order.setCouponCode(couponCode.getCode());
        }
        this.orderRepository.persist(order);

        final List<OrderItem> orderItems = new ArrayList<>(cart.getItems().size());
        for (ShoppingCartItemDetailedDto itemDto : cart.getItems()) {
            final OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(itemDto.getProduct().getId());
            orderItem.setPriceSnapshot(Objects.requireNonNullElse(
                    itemDto.getProduct().getPrice(), 0D
            ));
            orderItem.setQuantity(itemDto.getQuantity());

            orderItems.add(orderItem);
        }

        this.orderItemRepository.persistAll(orderItems);
        order.setItems(orderItems);

        // Do a final verification of the total price. Theoretically this should not happen.
        final Double orderTotal = this.calculateTotal(order);
        if (Double.compare(orderTotal, userAgreedPrice) != 0) {
            log.error("Order price differs from agreed use price! Order: {}, User Agreed: {}",
                    orderTotal,
                    userAgreedPrice
            );
            throw new ApiException(String.format(
                    "Order price differs from agreed use price! Order: %s, User Agreed: %s",
                    orderTotal,
                    userAgreedPrice
            ));
        }

        return order;
    }

    @Override
    @Transactional
    public void changeOrderStatus(UpdateOrderStatusDto dto) {
        final Order order = this.orderRepository.findById(dto.getOrder().getId());

        dto.getOrderStatus().getStatusApplier().applyOrderStatus(
                order,
                dto,
                this
        );

        this.orderRepository.merge(order);
        this.mailService.sendEmail(
                EmailTemplate.ORDER_STATUS_UPDATE,
                this.getOrder(order),
                List.of(order.getAddress().getEmail())
        );
    }

    @Override
    @Transactional
    public Order findById(Long id) {
        return this.orderRepository.findById(id);
    }

    @Override
    @Transactional
    public OrderDto getOrder(Long orderId) {
        final Order order = this.findById(orderId);
        return this.getOrder(order);
    }

    @Override
    @Transactional
    public OrderDto getOrder(Long orderId, Long userId) {
        final Order order = this.orderRepository.findByIdAndUserId(orderId, userId);
        return this.getOrder(order);
    }

    public OrderDto getOrder(Order order) {
        if (order == null) {
            return null;
        }

        final OrderDto orderDto = this.modelMapper.map(order, OrderDto.class);
        orderDto.setTotalPrice(this.calculateTotal(order));
        orderDto.setSubtotal(this.calculateSubtotal(order));

        final List<Long> productIds = orderDto.getItems().stream()
                .map(OrderItemDto::getProductId)
                .collect(Collectors.toList());

        final Map<Long, Product> prods = this.productService.findAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        orderDto.getItems().forEach(item -> item.setProduct(
                this.modelMapper.map(prods.get(item.getProductId()), ProductDto.class)
        ));

        return orderDto;
    }

    private Double calculateTotal(Order order) {
        final DoubleAdder da = new DoubleAdder();
        da.add(order.getDeliveryPrice());
        da.add(order.getTotalDiscounts() * -1.0);

        da.add(this.calculateSubtotal(order));

        return MathUtil.round(da.sum());
    }

    private Double calculateSubtotal(Order order) {
        final DoubleAdder da = new DoubleAdder();
        for (OrderItem item : order.getItems()) {
            da.add(MathUtil.calculatePrice(item.getPriceSnapshot(), item.getQuantity()));
        }

        return da.sum();
    }

    @Override
    @Transactional
    public Page<Order> searchOrders(OrderQuery query, Long userId) {
        Specification<Order> specification = OrderSpecifications.sort(query.getSort())
                .and(OrderSpecifications.statusContains(query.getStatuses()));

        if (query.isShowOnlyMine()) {
            specification = specification.and(OrderSpecifications.userIdEquals(userId));
        }

        if (query.getOrderId() != null) {
            specification = specification.and(OrderSpecifications.betweenId(query.getOrderId()));
        }

        return this.specificationExecutor.findAll(
                specification,
                query.getPage(),
                Order.class,
                null
        );
    }

    @Override
    public boolean isQuantitySufficient(Long productId, Integer quantity) {
        return this.productService.existsByIdAndMeetsQuantity(productId, quantity);
    }

    @Override
    public boolean updateStock(Long productId, Integer quantity, Order order) {
        return this.warehouseService.updateQuantity(order, productId, quantity);
    }
}
