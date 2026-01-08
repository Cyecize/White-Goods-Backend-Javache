package com.cyecize.app.api.user.constraints;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UsernameTakenValidator.class)
public @interface UsernameTaken {

    String message() default ValidationMessages.NAME_ALREADY_TAKEN;
}
