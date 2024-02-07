package com.cyecize.app.api.store.promotion.coupon;

import lombok.Data;

@Data
public class CouponCodeDto {

    private Long promotionId;

    private String code;
}
