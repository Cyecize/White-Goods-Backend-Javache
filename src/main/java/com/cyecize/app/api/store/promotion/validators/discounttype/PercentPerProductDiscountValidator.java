package com.cyecize.app.api.store.promotion.validators.discounttype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Component;

@Component
public class PercentPerProductDiscountValidator implements DiscountTypeValidator {

    @Override
    public void validate(CreatePromotionDto promo, BindingResult bindingResult) {
        if (promo.getDiscount() == null) {
            bindingResult.addNewError(new FieldError(
                    CreatePromotionDto.class.getName(),
                    "discount",
                    ValidationMessages.FIELD_CANNOT_BE_NULL,
                    null
            ));
        }
    }
}
