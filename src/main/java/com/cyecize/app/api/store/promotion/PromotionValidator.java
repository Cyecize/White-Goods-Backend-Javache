package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.api.store.promotion.validators.discounttype.DiscountTypeValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.PromotionTypeValidator;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PromotionValidator {

    private final BindingResult bindingResult;
    private final Map<DiscountType, DiscountTypeValidator> discountTypeValidatorMap;
    private final Map<PromotionType, PromotionTypeValidator> promotionTypePromotionTypeValidatorMap;

    public PromotionValidator(
            BindingResult bindingResult,
            List<DiscountTypeValidator> discountValidators,
            List<PromotionTypeValidator> promotionTypeValidators) {
        this.bindingResult = bindingResult;

        this.discountTypeValidatorMap = this.mapDiscountTypeValidators(discountValidators);
        this.promotionTypePromotionTypeValidatorMap = this.mapPromoTypeValidators(
                promotionTypeValidators
        );
    }

    public void validatePromoBusinessRules(CreatePromotionDto promo) {
        this.promotionTypePromotionTypeValidatorMap.get(promo.getPromotionType())
                .validate(promo, bindingResult);
        this.discountTypeValidatorMap.get(promo.getDiscountType())
                .validate(promo, bindingResult);

        if (this.bindingResult.hasErrors()) {
            throw new ConstraintValidationException(
                    "There are validation errors in the promo payload."
            );
        }
    }

    private Map<DiscountType, DiscountTypeValidator> mapDiscountTypeValidators(
            List<DiscountTypeValidator> discountTypeValidators) {
        final Map<DiscountType, DiscountTypeValidator> result = new HashMap<>();

        for (DiscountType discountType : DiscountType.values()) {
            final DiscountTypeValidator validator = discountTypeValidators.stream()
                    .filter(val -> discountType.getValidatorType().isAssignableFrom(val.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(String.format(
                            "Validator not found for discount type '%s'!",
                            discountType
                    )));

            result.put(discountType, validator);
        }

        return result;
    }

    private Map<PromotionType, PromotionTypeValidator> mapPromoTypeValidators(
            List<PromotionTypeValidator> promotionTypeValidators) {
        final Map<PromotionType, PromotionTypeValidator> result = new HashMap<>();

        for (PromotionType promoType : PromotionType.values()) {
            final PromotionTypeValidator validator = promotionTypeValidators.stream()
                    .filter(val -> promoType.getValidatorType().isAssignableFrom(val.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(String.format(
                            "Validator not found for promo type '%s'!",
                            promoType
                    )));

            result.put(promoType, validator);
        }

        return result;
    }
}
