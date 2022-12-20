package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.error.ApiException;

public class SentOrderStatusValidator implements OrderStatusValidator {

    @Override
    public void validate(UpdateOrderStatusDto dto, OrderService orderService) {
        if (dto.getOrder().getStatus() != OrderStatus.IN_PROGRESS) {
            throw new ApiException("Order must be in progress to send it!");
        }

        //TODO: check quantities
    }
}
