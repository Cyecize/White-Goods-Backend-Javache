package com.cyecize.app.api.store.order.orderstatus;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;

public class OrderSentStatusApplier implements OrderStatusApplier {

    @Override
    public void applyOrderStatus(Order order, UpdateOrderStatusDto dto, OrderService orderService) {
        order.setStatus(OrderStatus.SENT);
        order.setTrackingNumber(dto.getTrackingNumber());
        //TODO: update product quantities
    }
}
