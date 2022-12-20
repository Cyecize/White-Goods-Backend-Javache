package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleOrderStatusValidator implements OrderStatusValidator {

    private final boolean isValid;

    @Override
    public void validate(UpdateOrderStatusDto dto, OrderService orderService) {
        if (!this.isValid) {
            throw new ApiException(ValidationMessages.INVALID_VALUE);
        }
    }
}
