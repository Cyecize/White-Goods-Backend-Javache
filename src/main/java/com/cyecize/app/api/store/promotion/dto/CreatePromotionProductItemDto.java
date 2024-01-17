package com.cyecize.app.api.store.promotion.dto;

import com.cyecize.app.api.product.validator.ValidProductId;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePromotionProductItemDto {

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ValidProductId(message = ValidationMessages.INVALID_VALUE)
    private Long productId;

    @Min(value = 1, message = ValidationMessages.INVALID_VALUE)
    private Integer minQuantity;
}
