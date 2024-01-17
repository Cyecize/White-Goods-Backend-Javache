package com.cyecize.app.api.store.promotion.validators.promotype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Component;

@Component
public class DiscountOverSubtotalValidator implements PromotionTypeValidator {

    @Override
    public void validate(CreatePromotionDto promo, BindingResult bindingResult) {
        if (promo.getMinSubtotal() == null) {
            bindingResult.addNewError(new FieldError(
                    CreatePromotionDto.class.getName(),
                    "minSubtotal",
                    ValidationMessages.FIELD_CANNOT_BE_NULL,
                    null
            ));
        }
    }
}
