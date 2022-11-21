package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartService;
import com.cyecize.app.api.store.delivery.DeliveryAddress;
import com.cyecize.app.api.store.delivery.DeliveryAddressService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DeliveryAddressService addressService;

    private final ShoppingCartService shoppingCartService;

    @Configuration("delivery.price")
    private final Double deliveryPrince;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public void createOrder(CreateOrderAnonDto dto) {
        final List<ShoppingCartItemDetailedDto> items = this.shoppingCartService
                .getShoppingCartItems(dto.getSessionId(), false);

        if (items.isEmpty()) {
            throw new ApiException(ValidationMessages.SHOPPING_CART_EMPTY);
        }

        final DeliveryAddress address = this.addressService.createAddress(dto.getAddress());
        final Order order = this.createOrder(items, address, null);
        this.postOrderCreated(dto.getSessionId(), order, null);
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderLoggedInDto dto, User currentUser) {
        final List<ShoppingCartItemDetailedDto> items = this.shoppingCartService
                .getShoppingCartItems(dto.getSessionId(), true);

        if (items.isEmpty()) {
            throw new ApiException(ValidationMessages.SHOPPING_CART_EMPTY);
        }

        final DeliveryAddress address = this.addressService.createAddress(dto.getUserAddress());
        final Order order = this.createOrder(items, address, currentUser.getId());
        this.postOrderCreated(dto.getSessionId(), order, currentUser.getId());
    }

    private void postOrderCreated(String cartSessionId, Order order, Long userId) {
        this.shoppingCartService.removeAllItems(cartSessionId);
        //TODO: notify admins
    }

    private Order createOrder(List<ShoppingCartItemDetailedDto> items,
            DeliveryAddress address,
            Long userId) {
        final Order order = new Order();
        order.setAddressId(address.getId());

        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING);
        order.setUserId(userId);
        order.setDeliveryPrice(this.deliveryPrince);
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

        return order;
    }
}
