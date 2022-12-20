package com.cyecize.app.api.store.order.validator;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidUpdateOrderStatusDtoValidator implements
        ConstraintValidator<ValidUpdateOrderStatusDto, Order> {

    private final OrderService orderService;

    @Override
    public boolean isValid(Order order, Object model) {
        final UpdateOrderStatusDto dto = (UpdateOrderStatusDto) model;
        if (order == null || dto.getOrderStatus() == null) {
            return true;
        }

        dto.getOrderStatus().getValidator().validate(dto, orderService);
        return true;
    }
}
