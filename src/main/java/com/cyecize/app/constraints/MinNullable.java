package com.cyecize.app.constraints;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(
        validatedBy = MinNullableConstraint.class
)
public @interface MinNullable {

    String message() default ValidationMessages.INVALID_VALUE;

    double value();
}
