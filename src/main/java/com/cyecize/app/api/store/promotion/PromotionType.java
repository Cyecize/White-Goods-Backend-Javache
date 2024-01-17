package com.cyecize.app.api.store.promotion;

import static com.cyecize.app.api.store.promotion.PromotionStage.ADDITIONAL;
import static com.cyecize.app.api.store.promotion.PromotionStage.REGULAR;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountOverSubtotalFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountOverTotalFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificCategoryFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificProductsAllFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.DiscountSpecificProductsAnyFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.PromotionFilter;
import com.cyecize.app.api.store.promotion.promotionfilters.PromotionFilterBase;
import com.cyecize.app.api.store.promotion.validators.promotype.DiscountOverSubtotalValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.DiscountOverTotalValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.DiscountSpecificCategoryValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.DiscountSpecificProductsAllValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.DiscountSpecificProductsAnyValidator;
import com.cyecize.app.api.store.promotion.validators.promotype.PromotionTypeValidator;
import java.util.List;
import lombok.Getter;

public enum PromotionType {

    // Discount, applied if subtotal is over a given amount.
    DISCOUNT_OVER_SUBTOTAL(
            new DiscountOverSubtotalFilter(),
            REGULAR,
            DiscountOverSubtotalValidator.class
    ),

    // Discount, applied if the total is over a given amount.
    DISCOUNT_OVER_TOTAL(
            new DiscountOverTotalFilter(),
            ADDITIONAL,
            DiscountOverTotalValidator.class
    ),

    // Discount, applied if shopping cart contains any of the specified products.
    DISCOUNT_SPECIFIC_PRODUCTS_ANY(
            new DiscountSpecificProductsAnyFilter(),
            REGULAR,
            DiscountSpecificProductsAnyValidator.class
    ),

    // Discount, applied if shopping cart contains all the specified products.
    DISCOUNT_SPECIFIC_PRODUCTS_ALL(
            new DiscountSpecificProductsAllFilter(),
            REGULAR,
            DiscountSpecificProductsAllValidator.class
    ),

    // Discount, applied if shopping cart contains products from a given category.
    DISCOUNT_SPECIFIC_CATEGORY(
            new DiscountSpecificCategoryFilter(),
            REGULAR,
            DiscountSpecificCategoryValidator.class
    );
    // FUTURE: COUPON_CODE

    private final PromotionFilter filter;
    private final PromotionStage stage;
    @Getter
    private final Class<? extends PromotionTypeValidator> validatorType;

    PromotionType(PromotionFilterBase filter,
            PromotionStage stage,
            Class<? extends PromotionTypeValidator> validatorType) {
        this.filter = filter;
        this.stage = stage;
        filter.setPromotionType(this);
        this.validatorType = validatorType;
    }

    public boolean test(Promotion promotion, PriceBag priceBag) {
        this.verifyPromotionIsCompatible(promotion);
        return this.filter.test(promotion, priceBag);
    }

    public List<ShoppingCartItemDetailedDto> filterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        this.verifyPromotionIsCompatible(promotion);
        return this.filter.filterApplicableItems(promotion, items);
    }

    public PromotionStage getStage() {
        return this.stage;
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
