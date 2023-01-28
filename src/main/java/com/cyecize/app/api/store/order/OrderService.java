package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.order.dto.OrderDto;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.util.Page;

public interface OrderService {

    void createOrder(CreateOrderAnonDto dto);

    void createOrder(CreateOrderLoggedInDto dto, User currentUser);

    void changeOrderStatus(UpdateOrderStatusDto dto);

    Order findById(Long id);

    OrderDto getOrder(Long orderId);

    OrderDto getOrder(Long orderId, Long userId);

    Page<Order> searchOrders(OrderQuery query, Long userId);

    boolean isQuantitySufficient(Long productId, Integer quantity);

    boolean updateStock(Long productId, Integer quantity, Order order);
}
