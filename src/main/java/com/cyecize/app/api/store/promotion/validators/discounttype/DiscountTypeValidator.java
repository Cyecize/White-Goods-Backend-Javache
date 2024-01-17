package com.cyecize.app.api.store.promotion.validators.discounttype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;

public interface DiscountTypeValidator {

    void validate(CreatePromotionDto promo, BindingResult bindingResult);
}
