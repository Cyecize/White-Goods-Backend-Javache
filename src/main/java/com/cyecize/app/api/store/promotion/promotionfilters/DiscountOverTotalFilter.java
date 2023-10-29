package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;

public class DiscountOverTotalFilter extends PromotionFilterBase {

    @Override
    public boolean doTest(Promotion promotion, PriceBag priceBag) {
        if (priceBag.getTotal() == null) {
            throw new IllegalStateException("Discount over total type of promotion filter must "
                    + "be executed once total price was calculated!");
        }
        return priceBag.getTotal() > promotion.getMinSubtotal();
    }
}
