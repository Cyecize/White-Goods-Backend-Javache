package com.cyecize.app.api.product.validator;

import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@Component
@RequiredArgsConstructor
public class CanSeeHiddenProductsValidator implements
        ConstraintValidator<CanSeeHiddenProducts, Boolean> {

    private final Principal principal;

    @Override
    public boolean isValid(Boolean showHidden, Object model) {
        if (BooleanUtils.isNotTrue(showHidden)) {
            return true;
        }

        return principal.hasAuthority(General.ROLE_ADMIN);
    }
}
