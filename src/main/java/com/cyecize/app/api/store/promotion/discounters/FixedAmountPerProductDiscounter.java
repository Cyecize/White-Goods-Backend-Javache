package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDtoFactory;
import com.cyecize.app.api.store.promotion.dto.DiscountProductItemDto;
import com.cyecize.app.util.MathUtil;
import java.util.ArrayList;
import java.util.List;

public class FixedAmountPerProductDiscounter extends DiscounterBase {

    @Override
    protected void doApply(Promotion promotion, PriceBag priceBag) {
        final List<ShoppingCartItemDetailedDto> items = promotion.getPromotionType()
                .filterApplicableItems(promotion, priceBag.getItems());

        final List<DiscountProductItemDto> discountItems = new ArrayList<>();
        final List<Double> discountedValues = new ArrayList<>(items.size());
        for (ShoppingCartItemDetailedDto item : items) {
            final DiscountProductItemDto discountItem = new DiscountProductItemDto(
                    item.getProduct().getId(),
                    promotion.getDiscount(),
                    promotion.getDiscountType()
            );

            discountItems.add(discountItem);
            final double discount = MathUtil.calculatePrice(
                    promotion.getDiscount(),
                    item.getQuantity(),
                    false
            );

            discountedValues.add(discount);
        }

        final double totalDiscount = MathUtil.sumAll(discountedValues.toArray(Double[]::new));
        priceBag.addDiscount(DiscountDtoFactory.fromPromotion(promotion, totalDiscount, discountItems));
    }
}
