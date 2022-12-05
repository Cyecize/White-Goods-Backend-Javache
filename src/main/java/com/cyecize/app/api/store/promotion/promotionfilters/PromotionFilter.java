package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.Promotion;
import java.util.List;

public interface PromotionFilter {

    boolean test(Promotion promotion, FilterPayloadDto payload);

    List<ShoppingCartItemDetailedDto> filterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items);
}
