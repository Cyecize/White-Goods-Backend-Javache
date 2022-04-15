package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.api.product.converter.CategoryIdDataAdapter;
import com.cyecize.app.api.product.productspec.validator.UniqueSpecificationType;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateSpecificationTypeDto {
    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    @UniqueSpecificationType
    private String specificationType;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String titleBg;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String titleEn;

    @JsonProperty("categoryId")
    @ConvertedBy(CategoryIdDataAdapter.class)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private ProductCategory category;
}
