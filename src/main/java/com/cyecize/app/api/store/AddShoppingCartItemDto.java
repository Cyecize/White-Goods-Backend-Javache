package com.cyecize.app.api.store;

import com.cyecize.app.api.product.validator.ValidProductId;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.Max;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddShoppingCartItemDto {

    @ValidProductId
    private Long productId;

    @Min(value = 1, message = ValidationMessages.INVALID_VALUE)
    @Max(value = General.MAX_PROD_QUANTITY, message = ValidationMessages.INVALID_VALUE)
    private Integer quantity;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Boolean replace;
}
