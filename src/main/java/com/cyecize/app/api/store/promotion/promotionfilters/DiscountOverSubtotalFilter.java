package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;

public class DiscountOverSubtotalFilter extends PromotionFilterBase {

    @Override
    public boolean doTest(Promotion promotion, PriceBag priceBag) {
        return priceBag.getSubtotal() > promotion.getMinSubtotal();
    }
}
