package com.cyecize.app.api.store.promotion.coupon.validators;

import com.cyecize.app.api.store.promotion.coupon.CouponCodeService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Component
@RequiredArgsConstructor
public class ValidCouponCodeValidator implements ConstraintValidator<ValidCouponCode, String> {

    private final CouponCodeService couponCodeService;

    @Override
    public boolean isValid(String code, Object o) {
        code = StringUtils.trimToNull(code);
        if (code == null) {
            return false;
        }

        return this.couponCodeService.getValidCouponCode(code).isPresent();
    }
}
