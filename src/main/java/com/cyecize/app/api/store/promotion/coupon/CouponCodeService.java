package com.cyecize.app.api.store.promotion.coupon;

import java.util.List;

public interface CouponCodeService {

    void deleteAllDisabledCodes();

    boolean useCouponCode(String code);

    CouponCodeDto getValidCouponCode(String code);

    List<CouponCodeDto> createCouponCodes(CreateCouponCodeDto dto);
}
