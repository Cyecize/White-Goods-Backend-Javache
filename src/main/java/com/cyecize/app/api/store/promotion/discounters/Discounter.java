package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;

public interface Discounter {

    DiscountDto applyDiscount(Promotion promotion, DiscounterPayloadDto payload);
}
