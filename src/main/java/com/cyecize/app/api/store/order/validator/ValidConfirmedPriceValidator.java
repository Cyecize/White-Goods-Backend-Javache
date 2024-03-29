package com.cyecize.app.api.store.order.validator;

import com.cyecize.app.api.store.pricing.Price;
import com.cyecize.app.api.store.pricing.PricingService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import java.lang.reflect.Field;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidConfirmedPriceValidator implements
        ConstraintValidator<ValidConfirmedPrice, Double> {

    private final PricingService pricingService;

    private String sessionIdFieldName;

    @Override
    public void initialize(ValidConfirmedPrice constraintAnnotation) {
        this.sessionIdFieldName = constraintAnnotation.sessionIdFieldName();
    }

    @Override
    public boolean isValid(Double val, Object model) {
        if (val == null) {
            return true;
        }
        final String sessionId;
        try {
            final Field declaredField = model.getClass().getDeclaredField(this.sessionIdFieldName);
            declaredField.setAccessible(true);
            sessionId = (String) declaredField.get(model);
        } catch (Exception e) {
            System.out.printf(
                    "Error while validating confirm price on dto %s. Field: %s, User Val: %s%n",
                    model, this.sessionIdFieldName, val
            );
            e.printStackTrace();
            return false;
        }

        final Price price = this.pricingService.getPrice(sessionId);
        return Double.compare(val, price.getTotal()) == 0;
    }
}
