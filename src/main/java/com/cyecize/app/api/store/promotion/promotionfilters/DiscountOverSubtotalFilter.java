package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.promotion.Promotion;

public class DiscountOverSubtotalFilter extends PromotionFilterBase {

    @Override
    public boolean doTest(Promotion promotion, FilterPayloadDto payload) {
        return payload.getSubtotal() > promotion.getMinSubtotal();
    }
}
