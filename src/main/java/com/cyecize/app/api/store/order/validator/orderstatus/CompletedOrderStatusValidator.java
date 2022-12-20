package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.error.ApiException;

public class CompletedOrderStatusValidator implements OrderStatusValidator {

    @Override
    public void validate(UpdateOrderStatusDto dto, OrderService orderService) {
        if (dto.getOrder().getStatus() != OrderStatus.SENT) {
            throw new ApiException(
                    "Order must be sent before it can be completed, you may want to cancel it."
            );
        }
    }
}
