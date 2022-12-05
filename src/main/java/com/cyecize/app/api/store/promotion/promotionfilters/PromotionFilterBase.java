package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.PromotionType;
import java.util.ArrayList;
import java.util.List;

public abstract class PromotionFilterBase implements PromotionFilter {

    private PromotionType promotionType;

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    @Override
    public boolean test(Promotion promotion, FilterPayloadDto payload) {
        if (promotion.getPromotionType() != this.promotionType) {
            throw new IllegalArgumentException(String.format(
                    "Trying to test %s on filter for %s promotion type.",
                    promotion.getPromotionType(),
                    this.promotionType
            ));
        }

        return this.doTest(promotion, payload);
    }

    protected abstract boolean doTest(Promotion promotion, FilterPayloadDto payload);

    @Override
    public List<ShoppingCartItemDetailedDto> filterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        if (promotion.getPromotionType() != this.promotionType) {
            throw new IllegalArgumentException(String.format(
                    "Trying to pass promotion with type %s on filter for %s promotion type.",
                    promotion.getPromotionType(),
                    this.promotionType
            ));
        }

        return this.doFilterApplicableItems(promotion, items);
    }

    public List<ShoppingCartItemDetailedDto> doFilterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        return new ArrayList<>(items);
    }
}
