package com.cyecize.app.api.store.order.dto;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.api.store.order.converter.OrderIdDataAdapter;
import com.cyecize.app.api.store.order.validator.ValidUpdateOrderStatusDto;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.converters.GenericEnumConverter;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {

    @ValidUpdateOrderStatusDto
    @ConvertedBy(OrderIdDataAdapter.class)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Order order;

    @GenericEnumConverter
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private OrderStatus orderStatus;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String trackingNumber;
}
