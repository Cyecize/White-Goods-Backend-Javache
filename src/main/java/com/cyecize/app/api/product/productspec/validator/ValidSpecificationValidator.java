package com.cyecize.app.api.product.productspec.validator;

import com.cyecize.app.api.product.productspec.SpecificationTypeService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidSpecificationValidator implements
        ConstraintValidator<ValidSpecificationType, Long> {

    private final SpecificationTypeService specificationTypeService;

    @Override
    public boolean isValid(Long value, Object bindingModel) {
        if (value == null) {
            return true;
        }

        return this.specificationTypeService.specificationTypeExists(value);
    }
}
