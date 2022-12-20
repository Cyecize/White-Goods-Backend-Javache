package com.cyecize.app.api.store.order.orderstatus;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;

public interface OrderStatusApplier {

    void applyOrderStatus(Order order, UpdateOrderStatusDto dto, OrderService orderService);
}
