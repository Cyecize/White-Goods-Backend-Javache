package com.cyecize.app.api.product.selection;

import com.cyecize.app.api.product.validator.ValidProductId;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductSelectionDto {

    @ValidProductId
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Long id;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Long orderNumber;
}
