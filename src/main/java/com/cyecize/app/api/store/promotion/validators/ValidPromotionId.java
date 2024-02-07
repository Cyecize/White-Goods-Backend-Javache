package com.cyecize.app.api.store.promotion.validators;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidPromotionIdValidator.class)
public @interface ValidPromotionId {

    String message() default ValidationMessages.INVALID_VALUE;
}
