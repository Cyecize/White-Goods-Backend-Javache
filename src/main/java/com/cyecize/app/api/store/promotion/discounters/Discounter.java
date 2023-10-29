package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;

public interface Discounter {

    void applyDiscount(Promotion promotion, PriceBag priceBag);
}
