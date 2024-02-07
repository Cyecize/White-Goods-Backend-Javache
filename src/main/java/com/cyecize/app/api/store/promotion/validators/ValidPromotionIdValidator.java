package com.cyecize.app.api.store.promotion.validators;

import com.cyecize.app.api.store.promotion.PromotionService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidPromotionIdValidator implements ConstraintValidator<ValidPromotionId, Long> {

    private final PromotionService promotionService;

    @Override
    public boolean isValid(Long id, Object bindingModel) {
        if (id == null) {
            return false;
        }

        return this.promotionService.existsById(id);
    }
}
