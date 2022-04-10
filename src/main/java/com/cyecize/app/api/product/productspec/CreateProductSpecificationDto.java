package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.productspec.validator.ValidSpecificationType;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductSpecificationDto {
    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String valueBg;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String valueEn;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ValidSpecificationType
    private Long specificationTypeId;
}
