package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscountSpecificCategoryFilter extends PromotionFilterBase {

    @Override
    protected boolean doTest(Promotion promotion, PriceBag priceBag) {
        return !this.doFilterApplicableItems(
                promotion, priceBag.getShoppingCart().getItems()
        ).isEmpty();
    }

    @Override
    public List<ShoppingCartItemDetailedDto> doFilterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        return items.stream()
                .filter(item -> Objects.equals(
                        promotion.getCategoryId(), item.getProduct().getCategoryId()
                ))
                .collect(Collectors.toList());
    }
}
