package com.cyecize.app.api.store.order;

import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartPricingDto;
import com.cyecize.app.api.store.cart.ShoppingCartService;
import com.cyecize.app.api.store.delivery.DeliveryAddress;
import com.cyecize.app.api.store.delivery.DeliveryAddressService;
import com.cyecize.app.api.store.order.dto.OrderDto;
import com.cyecize.app.api.store.order.dto.OrderItemDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.UserService;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.MathUtil;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DeliveryAddressService addressService;

    private final ShoppingCartService shoppingCartService;

    @Configuration("delivery.price")
    private final Double deliveryPrince;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ModelMapper modelMapper;

    private final ProductService productService;

    private final UserService userService;

    private final MailService mailService;

    private final SpecificationExecutor specificationExecutor;

    @Override
    @Transactional
    public void createOrder(CreateOrderAnonDto dto) {
        final List<ShoppingCartItemDetailedDto> items = this.shoppingCartService
                .getShoppingCartItems(dto.getSessionId(), false);

        if (items.isEmpty()) {
            throw new ApiException(ValidationMessages.SHOPPING_CART_EMPTY);
        }

        final DeliveryAddress address = this.addressService.createAddress(dto.getAddress());
        final Order order = this.createOrder(
                items, address, dto.getSessionId(), null, dto.getUserAgreedPrice()
        );
        this.postOrderCreated(dto.getSessionId(), order, null);
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderLoggedInDto dto, User currentUser) {
        final List<ShoppingCartItemDetailedDto> items = this.shoppingCartService
                .getShoppingCartItems(dto.getSessionId(), false);

        if (items.isEmpty()) {
            throw new ApiException(ValidationMessages.SHOPPING_CART_EMPTY);
        }

        final DeliveryAddress address = this.addressService.createAddress(dto.getUserAddress());
        final Order order = this.createOrder(
                items, address, dto.getSessionId(), currentUser.getId(), dto.getUserAgreedPrice()
        );
        this.postOrderCreated(dto.getSessionId(), order, currentUser.getId());
    }

    private void postOrderCreated(String cartSessionId, Order order, Long userId) {
        this.shoppingCartService.removeAllItems(cartSessionId);
        final List<String> emailsOfAdmins = this.userService.getEmailsOfAdmins();
        final OrderDto orderDto = this.getOrder(order.getId());

        this.mailService.sendEmail(EmailTemplate.NEW_ORDER_ADMINS, orderDto, emailsOfAdmins);
    }

    private Order createOrder(List<ShoppingCartItemDetailedDto> items,
            DeliveryAddress address,
            String cartSessionId,
            Long userId,
            Double userAgreedPrice) {
        final ShoppingCartPricingDto pricing = this.shoppingCartService.getPricing(cartSessionId);
        final Order order = new Order();
        order.setAddressId(address.getId());
        order.setAddress(address);

        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING);
        order.setUserId(userId);
        order.setDeliveryPrice(pricing.getDiscounts().isFreeDelivery() ? 0D : this.deliveryPrince);
        order.setTotalDiscounts(pricing.getDiscounts().getTotalDiscounts());
        this.orderRepository.persist(order);

        final List<OrderItem> orderItems = new ArrayList<>(items.size());
        for (ShoppingCartItemDetailedDto itemDto : items) {
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
    public OrderDto getOrder(Long orderId) {
        final Order order = this.orderRepository.findById(orderId);
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
        final Specification<Order> specification = OrderSpecifications.userIdEquals(userId)
                .and(OrderSpecifications.sort(query.getSort()))
                .and(OrderSpecifications.statusContains(query.getStatuses()));

        return this.specificationExecutor.findAll(
                specification,
                query.getPage(),
                Order.class,
                null
        );
    }
}
