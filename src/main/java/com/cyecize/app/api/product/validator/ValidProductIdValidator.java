package com.cyecize.app.api.product.validator;

import com.cyecize.app.api.product.ProductService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidProductIdValidator implements ConstraintValidator<ValidProductId, Long> {

    private final ProductService productService;

    @Override
    public boolean isValid(Long id, Object bindingModel) {
        if (id == null) {
            return false;
        }

        return this.productService.existsById(id);
    }
}
