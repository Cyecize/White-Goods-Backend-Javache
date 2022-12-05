package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountOverSubtotalFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificCategoryFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificProductsAllFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificProductsAnyFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.FilterPayloadDto;
import com.cyecize.app.api.store.promotion.promotionfilters.PromotionFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.PromotionFilterBase;
import java.util.List;

public enum PromotionType {

    // Discount, applied if subtotal is over a given amount.
    DISCOUNT_OVER_SUBTOTAL(new DiscountOverSubtotalFilter()),

    // Discount, applied if shopping cart contains any of the specified products.
    DISCOUNT_SPECIFIC_PRODUCTS_ANY(new DiscountSpecificProductsAnyFilter()),

    // Discount, applied if shopping cart contains all the specified products.
    DISCOUNT_SPECIFIC_PRODUCTS_ALL(new DiscountSpecificProductsAllFilter()),

    // Discount, applied if shopping cart contains products from a given category.
    DISCOUNT_SPECIFIC_CATEGORY(new DiscountSpecificCategoryFilter());
    // FUTURE: COUPON_CODE

    private final PromotionFilter filter;

    PromotionType(PromotionFilterBase filter) {
        this.filter = filter;
        filter.setPromotionType(this);
    }

    public boolean test(Promotion promotion, FilterPayloadDto payload) {
        this.verifyPromotionIsCompatible(promotion);
        return this.filter.test(promotion, payload);
    }

    public List<ShoppingCartItemDetailedDto> filterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        this.verifyPromotionIsCompatible(promotion);
        return this.filter.filterApplicableItems(promotion, items);
    }

    private void verifyPromotionIsCompatible(Promotion promotion) {
        if (!this.equals(promotion.getPromotionType())) {
            throw new IllegalArgumentException(String.format(
                    "Promotion with type %s is not compatible with %s type enum.",
                    promotion.getPromotionType(),
                    this
            ));
        }
    }
}
