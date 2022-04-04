package com.cyecize.app.api.base64.validator;

import com.cyecize.summer.areas.validation.annotations.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ImageTypeFileValidator.class)
public @interface ImageTypeFile {
    String message() default "Expecting image type";
}
