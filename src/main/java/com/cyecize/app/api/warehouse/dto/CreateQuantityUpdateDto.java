package com.cyecize.app.api.warehouse.dto;

import com.cyecize.app.api.product.validator.ValidProductId;
import com.cyecize.app.api.warehouse.QuantityUpdateType;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.converters.GenericEnumConverter;
import com.cyecize.summer.areas.validation.constraints.Max;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateQuantityUpdateDto {

    @ValidProductId
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Long productId;

    @GenericEnumConverter
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private QuantityUpdateType updateType;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @Max(value = Integer.MAX_VALUE, message = ValidationMessages.INVALID_VALUE)
    private Integer quantityValue;
}
