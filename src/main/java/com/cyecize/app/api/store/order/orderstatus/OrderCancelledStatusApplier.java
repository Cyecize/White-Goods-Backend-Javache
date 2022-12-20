package com.cyecize.app.api.store.order.orderstatus;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import java.time.LocalDateTime;

public class OrderCancelledStatusApplier implements OrderStatusApplier {

    @Override
    public void applyOrderStatus(Order order, UpdateOrderStatusDto dto, OrderService orderService) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setDateCompleted(LocalDateTime.now());
    }
}
