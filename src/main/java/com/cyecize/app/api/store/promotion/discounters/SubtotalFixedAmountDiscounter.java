package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import com.cyecize.app.api.store.promotion.dto.DiscountDtoFactory;

public class SubtotalFixedAmountDiscounter extends DiscounterBase {

    @Override
    protected DiscountDto doApply(Promotion promotion, DiscounterPayloadDto payload) {
        return DiscountDtoFactory.fromPromotion(promotion, promotion.getDiscount());
    }
}
