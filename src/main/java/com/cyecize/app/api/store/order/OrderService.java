package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.order.dto.OrderDto;
import com.cyecize.app.api.user.User;

public interface OrderService {

    void createOrder(CreateOrderAnonDto dto);

    void createOrder(CreateOrderLoggedInDto dto, User currentUser);

    OrderDto getOrder(Long orderId);

}
