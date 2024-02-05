package com.cyecize.app.api.store.promotion.dto;

import com.cyecize.app.api.store.promotion.DiscountType;
import com.cyecize.app.api.store.promotion.PromotionType;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.converters.GenericEnumConverter;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CreatePromotionDto {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String nameBg;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String nameEn;

    @GenericEnumConverter
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private PromotionType promotionType;

    @GenericEnumConverter
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private DiscountType discountType;

    private Long categoryId;

    private Double discount;

    private Double minSubtotal;

    @Valid
    private List<CreatePromotionProductItemDto> productItems;
}
