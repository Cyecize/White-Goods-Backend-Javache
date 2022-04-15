package com.cyecize.app.api.product.productspec.validator;

import com.cyecize.app.api.product.productspec.SpecificationTypeService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Component
@RequiredArgsConstructor
public class UniqueSpecificationTypeValidator implements ConstraintValidator<UniqueSpecificationType, String> {
    private final SpecificationTypeService specificationTypeService;

    @Override
    public boolean isValid(String fieldValue, Object bindingModel) {
        if (StringUtils.trimToNull(fieldValue) == null) {
            return true;
        }

        return !this.specificationTypeService.existsBySpecificationType(fieldValue);
    }
}
