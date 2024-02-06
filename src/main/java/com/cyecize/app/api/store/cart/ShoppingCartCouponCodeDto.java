package com.cyecize.app.api.store.cart;

import lombok.Data;

@Data
public class ShoppingCartCouponCodeDto {

    private final String code;
    private final Long promotionId;
}
