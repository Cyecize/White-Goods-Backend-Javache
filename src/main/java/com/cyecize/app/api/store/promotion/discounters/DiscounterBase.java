package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.DiscountType;
import com.cyecize.app.api.store.promotion.Promotion;


public abstract class DiscounterBase implements Discounter {

    private DiscountType discountType;

    @Override
    public final void applyDiscount(Promotion promotion, PriceBag priceBag) {
        if (promotion.getDiscountType() != this.discountType) {
            throw new IllegalArgumentException(String.format(
                    "Trying to apply discount type %s on discounter for %s type.",
                    promotion.getDiscountType(),
                    this.discountType
            ));
        }

        this.doApply(promotion, priceBag);
    }

    protected abstract void doApply(Promotion promotion, PriceBag priceBag);

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
