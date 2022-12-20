package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.error.ApiException;

public class CancelledOrderStatusValidator implements OrderStatusValidator {

    @Override
    public void validate(UpdateOrderStatusDto dto, OrderService orderService) {
        final OrderStatus status = dto.getOrder().getStatus();
        if (status == OrderStatus.SENT || status == OrderStatus.COMPLETED) {
            throw new ApiException("Too late to cancel the order.");
        }
    }
}
