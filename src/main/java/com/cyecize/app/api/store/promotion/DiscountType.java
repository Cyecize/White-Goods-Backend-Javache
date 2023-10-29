package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.discounters.Discounter;
import com.cyecize.app.api.store.promotion.discounters.DiscounterBase;
import com.cyecize.app.api.store.promotion.discounters.FixedAmountPerProductDiscounter;
import com.cyecize.app.api.store.promotion.discounters.FreeDeliveryDiscounter;
import com.cyecize.app.api.store.promotion.discounters.PercentPerProductDiscounter;
import com.cyecize.app.api.store.promotion.discounters.SubtotalFixedAmountDiscounter;
import lombok.Getter;

public enum DiscountType {
    //Apply price discount based on a percentage value for each qualifying product.
    PERCENT_PER_PRODUCT(0, new PercentPerProductDiscounter()),
    FIXED_AMOUNT_PER_PRODUCT(0, new FixedAmountPerProductDiscounter()),

    // Apply price discount to the final price based on a fixed value.
    SUBTOTAL_FIXED_AMOUNT(1, new SubtotalFixedAmountDiscounter()),
    FREE_DELIVERY(1, new FreeDeliveryDiscounter());

    @Getter
    private final int executionOrder;
    private final Discounter discounter;

    DiscountType(int executionOrder, DiscounterBase discounter) {
        this.executionOrder = executionOrder;
        discounter.setDiscountType(this);
        this.discounter = discounter;
    }

    public void applyDiscount(Promotion promotion, PriceBag priceBag) {
        if (!this.equals(promotion.getDiscountType())) {
            throw new IllegalArgumentException(String.format(
                    "Trying to apply discount with type %s but promotion with type %s is used",
                    this,
                    promotion.getDiscountType()
            ));
        }

        this.discounter.applyDiscount(promotion, priceBag);
    }
}
