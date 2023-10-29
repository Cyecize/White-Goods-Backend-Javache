package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDtoFactory;

public class FreeDeliveryDiscounter extends DiscounterBase {

    @Override
    protected void doApply(Promotion promotion, PriceBag priceBag) {
        priceBag.addDiscount(DiscountDtoFactory.fromPromotion(promotion, true));
    }
}
