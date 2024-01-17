package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.discounters.Discounter;
import com.cyecize.app.api.store.promotion.discounters.DiscounterBase;
import com.cyecize.app.api.store.promotion.discounters.FixedAmountPerProductDiscounter;
import com.cyecize.app.api.store.promotion.discounters.FreeDeliveryDiscounter;
import com.cyecize.app.api.store.promotion.discounters.PercentPerProductDiscounter;
import com.cyecize.app.api.store.promotion.discounters.SubtotalFixedAmountDiscounter;
import com.cyecize.app.api.store.promotion.validators.discounttype.DiscountTypeValidator;
import com.cyecize.app.api.store.promotion.validators.discounttype.FixedAmountPerProductDiscountValidator;
import com.cyecize.app.api.store.promotion.validators.discounttype.FreeDeliveryDiscountValidator;
import com.cyecize.app.api.store.promotion.validators.discounttype.PercentPerProductDiscountValidator;
import com.cyecize.app.api.store.promotion.validators.discounttype.SubtotalFixedAmountDiscountValidator;
import lombok.Getter;

public enum DiscountType {
    //Apply price discount based on a percentage value for each qualifying product.
    PERCENT_PER_PRODUCT(
            0,
            new PercentPerProductDiscounter(),
            PercentPerProductDiscountValidator.class
    ),
    FIXED_AMOUNT_PER_PRODUCT(
            0,
            new FixedAmountPerProductDiscounter(),
            FixedAmountPerProductDiscountValidator.class
    ),

    // Apply price discount to the final price based on a fixed value.
    SUBTOTAL_FIXED_AMOUNT(
            1,
            new SubtotalFixedAmountDiscounter(),
            SubtotalFixedAmountDiscountValidator.class
    ),
    FREE_DELIVERY(
            1,
            new FreeDeliveryDiscounter(),
            FreeDeliveryDiscountValidator.class
    );

    @Getter
    private final int executionOrder;
    private final Discounter discounter;
    @Getter
    private final Class<? extends DiscountTypeValidator> validatorType;

    DiscountType(int executionOrder,
            DiscounterBase discounter,
            Class<? extends DiscountTypeValidator> validatorType) {
        this.executionOrder = executionOrder;
        discounter.setDiscountType(this);
        this.discounter = discounter;
        this.validatorType = validatorType;
    }

    public void applyDiscount(Promotion promotion, PriceBag priceBag) {
        if (!this.equals(promotion.getDiscountType())) {
            throw new IllegalArgumentException(String.format(
                    "Trying to apply discount with type %s but promotion with type %s is used",
                    this,
                    promotion.getDiscountType()
            ));
        }

        this.discounter.applyDiscount(promotion, priceBag);
    }
}
