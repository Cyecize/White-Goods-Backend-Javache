package com.cyecize.app.constraints;

import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;

@Component
public class MinNullableConstraint implements ConstraintValidator<MinNullable, Object> {

    private double minValue;

    public MinNullableConstraint() {
    }

    public void initialize(MinNullable constraintAnnotation) {
        this.minValue = constraintAnnotation.value();
    }

    public boolean isValid(Object field, Object bindingModel) {
        double val = 0.0;
        if (field != null) {
            val = Double.valueOf(String.valueOf(field));
        }

        return this.minValue <= val;
    }
}
