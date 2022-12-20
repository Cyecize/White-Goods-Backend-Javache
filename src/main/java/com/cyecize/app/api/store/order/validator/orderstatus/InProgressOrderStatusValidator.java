package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.error.ApiException;

public class InProgressOrderStatusValidator implements OrderStatusValidator {

    @Override
    public void validate(UpdateOrderStatusDto dto, OrderService orderService) {
        if (dto.getOrder().getStatus() != OrderStatus.WAITING) {
            throw new ApiException("Order must be in waiting status in order to start progress.");
        }
    }
}
