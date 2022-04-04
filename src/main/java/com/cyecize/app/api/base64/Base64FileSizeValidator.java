package com.cyecize.app.api.base64;

import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.enums.ServiceLifeSpan;

@Component(lifespan = ServiceLifeSpan.REQUEST)
public class Base64FileSizeValidator implements ConstraintValidator<Base64FileSize, String> {

    private int max;
    private int min;

    @Override
    public void initialize(Base64FileSize constraintAnnotation) {
        this.max = constraintAnnotation.maxBytes();
        this.min = constraintAnnotation.minBytes();
    }

    @Override
    public boolean isValid(String value, Object bindingModel) {
        if (value == null) {
            return true;
        }

        //Formula for calculating the size of a base64 file.
        final int y = value.endsWith("==") ? 2 : 1;
        final double fileSize = (value.length() * (3.0 / 4.0)) - y;

        return fileSize >= this.min && fileSize <= this.max;
    }
}
