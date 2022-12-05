package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.promotion.DiscountType;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;


public abstract class DiscounterBase implements Discounter {

    private DiscountType discountType;

    @Override
    public DiscountDto applyDiscount(Promotion promotion, DiscounterPayloadDto payload) {
        if (promotion.getDiscountType() != this.discountType) {
            throw new IllegalArgumentException(String.format(
                    "Trying to apply discount type %s on discounter for %s type.",
                    promotion.getDiscountType(),
                    this.discountType
            ));
        }

        return this.doApply(promotion, payload);
    }

    protected abstract DiscountDto doApply(Promotion promotion, DiscounterPayloadDto payload);

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
