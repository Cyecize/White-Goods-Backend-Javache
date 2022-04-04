package com.cyecize.app.api.base64;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64FileSizeValidator.class)
public @interface Base64FileSize {
    int minBytes() default 0;

    int maxBytes() default Integer.MAX_VALUE;

    String message() default ValidationMessages.INVALID_FILE_SIZE;
}
