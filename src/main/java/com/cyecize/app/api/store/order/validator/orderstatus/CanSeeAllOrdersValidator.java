package com.cyecize.app.api.store.order.validator.orderstatus;

import com.cyecize.app.api.store.order.validator.CanSeeAllOrders;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@Component
@RequiredArgsConstructor
public class CanSeeAllOrdersValidator implements ConstraintValidator<CanSeeAllOrders, Boolean> {

    private final Principal principal;

    @Override
    public boolean isValid(Boolean showOnlyMine, Object model) {
        if (BooleanUtils.isTrue(showOnlyMine)) {
            return true;
        }

        return principal.hasAuthority(General.ROLE_ADMIN);
    }
}
