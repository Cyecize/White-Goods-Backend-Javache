package com.cyecize.app.api.store.promotion;

public enum DiscountType {
    //Apply fixed price discount based on the total value of the items in the cart.
    SUBTOTAL_FIXED_PERCENT,

    //Apply varying price discount based on the final price that may include shipping fee and other discounts.
    TOTAL_VARYING_PERCENT,
    FIXED,
    FREE_DELIVERY
}
