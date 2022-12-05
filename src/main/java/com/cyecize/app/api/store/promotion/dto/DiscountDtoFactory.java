package com.cyecize.app.api.store.promotion.dto;

import com.cyecize.app.api.store.promotion.Promotion;
import java.util.ArrayList;
import java.util.List;

public class DiscountDtoFactory {

    public static DiscountDto fromPromotion(Promotion promotion, Double value) {
        return fromPromotion(promotion, value, false, new ArrayList<>());
    }

    public static DiscountDto fromPromotion(Promotion promotion, boolean freeDelivery) {
        return fromPromotion(promotion, null, freeDelivery, new ArrayList<>());
    }

    public static DiscountDto fromPromotion(Promotion promotion,
            Double value,
            List<DiscountProductItemDto> items) {
        return fromPromotion(promotion, value, false, items);
    }

    private static DiscountDto fromPromotion(Promotion promotion,
            Double value,
            boolean freeDelivery,
            List<DiscountProductItemDto> items) {
        return new DiscountDto(
                promotion.getNameBg(),
                promotion.getNameEn(),
                value,
                freeDelivery,
                items
        );
    }
}
