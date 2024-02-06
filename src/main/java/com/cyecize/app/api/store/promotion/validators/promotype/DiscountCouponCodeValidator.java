package com.cyecize.app.api.store.promotion.validators.promotype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Component;

@Component
public class DiscountCouponCodeValidator implements PromotionTypeValidator {

    @Override
    public void validate(CreatePromotionDto promo, BindingResult bindingResult) {
        // Nothing to validate since coupon codes are kept outside.
    }
}
