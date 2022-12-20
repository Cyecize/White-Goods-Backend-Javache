package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;

public interface OrderStatusValidator {

    void validate(UpdateOrderStatusDto dto, OrderService orderService);
}
