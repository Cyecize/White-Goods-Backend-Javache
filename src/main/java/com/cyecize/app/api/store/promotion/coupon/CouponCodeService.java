package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.util.Page;
import java.util.List;
import java.util.Optional;

public interface CouponCodeService {

    void deleteAllDisabledCodes();

    boolean useCouponCode(String code);

    Optional<CouponCodeDto> getValidCouponCode(String code);

    List<CouponCodeDto> createCouponCodes(CreateCouponCodeDto dto);

    Page<CouponCode> search(CouponCodeQuery query);
}
