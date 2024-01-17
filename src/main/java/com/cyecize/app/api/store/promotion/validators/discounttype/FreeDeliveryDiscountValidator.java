package com.cyecize.app.api.store.promotion.validators.discounttype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Component;

@Component
public class FreeDeliveryDiscountValidator implements DiscountTypeValidator {

    @Override
    public void validate(CreatePromotionDto promo, BindingResult bindingResult) {
        // Nothing additional required for free delivery
    }
}
