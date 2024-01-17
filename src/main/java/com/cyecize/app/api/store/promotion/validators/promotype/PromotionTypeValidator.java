package com.cyecize.app.api.store.promotion.validators.promotype;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;

public interface PromotionTypeValidator {

    void validate(CreatePromotionDto promo, BindingResult bindingResult);
}
