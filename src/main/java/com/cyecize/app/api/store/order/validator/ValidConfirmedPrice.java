package com.cyecize.app.api.store.order.validator;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidConfirmedPriceValidator.class)
public @interface ValidConfirmedPrice {

    String message() default ValidationMessages.ORDER_INVALID_CONFIRM_PRICE;

    String sessionIdFieldName() default "sessionId";
}
