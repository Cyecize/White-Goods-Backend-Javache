package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;
import java.util.Objects;

public class DiscountCouponCodeFilter extends PromotionFilterBase {

    @Override
    protected boolean doTest(Promotion promotion, PriceBag priceBag) {
        if (priceBag.getShoppingCart().getCouponCode() == null) {
            return false;
        }

        return Objects.equals(
                promotion.getId(),
                priceBag.getShoppingCart().getCouponCode().getPromotionId()
        );
    }
}
