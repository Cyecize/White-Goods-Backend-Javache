package com.cyecize.app.api.product.dto;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.app.api.base64.validator.ImageTypeFile;
import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.api.product.converter.CategoryIdDataAdapter;
import com.cyecize.app.api.product.productspec.CreateProductSpecificationDto;
import com.cyecize.app.api.product.productspec.ProductSpecification;
import com.cyecize.app.api.product.productspec.ProductSpecificationIdArrayDataAdapter;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class CreateProductDto {

    @JsonProperty("categoryId")
    @NotNull(message = ValidationMessages.INVALID_VALUE)
    @ConvertedBy(CategoryIdDataAdapter.class)
    private ProductCategory category;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String productName;

    private Double price;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String descriptionEn;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String descriptionBg;

    @Valid
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ImageTypeFile(message = ValidationMessages.INVALID_VALUE)
    private Base64FileBindingModel image;

    @MaxLength(length = General.MAX_ALLOWED_TAGS, message = ValidationMessages.TOO_MANY_TAGS)
    private List<String> tagNames;

    private Boolean enabled;

    @Min(value = General.MIN_PROD_QUANTITY, message = ValidationMessages.INVALID_VALUE)
    private Integer initialQuantity;

    @Valid
    private List<CreateProductSpecificationDto> productSpecifications;

    @NotNull
    @ConvertedBy(ProductSpecificationIdArrayDataAdapter.class)
    private List<ProductSpecification> existingProductSpecifications;

    @Valid
    private List<Base64FileBindingModel> gallery;

    public Integer getInitialQuantity() {
        return Objects.requireNonNullElse(this.initialQuantity, 0);
    }
}
