package com.cyecize.app.api.store.order.orderstatus;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.error.ApiException;

public class OrderSentStatusApplier implements OrderStatusApplier {

    @Override
    public void applyOrderStatus(Order order, UpdateOrderStatusDto dto, OrderService orderService) {
        order.setStatus(OrderStatus.SENT);
        order.setTrackingNumber(dto.getTrackingNumber());

        order.getItems().forEach(i -> {
            final boolean succ = orderService.updateStock(i.getProductId(), i.getQuantity(), order);
            if (!succ) {
                throw new ApiException(String.format(
                        "Could not update product %s's qty!", i.getProductId())
                );
            }
        });
    }
}
