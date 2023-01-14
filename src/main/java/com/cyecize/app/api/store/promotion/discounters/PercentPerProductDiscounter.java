package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import com.cyecize.app.api.store.promotion.dto.DiscountDtoFactory;
import com.cyecize.app.api.store.promotion.dto.DiscountProductItemDto;
import com.cyecize.app.util.MathUtil;
import java.util.ArrayList;
import java.util.List;

public class PercentPerProductDiscounter extends DiscounterBase {

    @Override
    protected DiscountDto doApply(Promotion promotion, DiscounterPayloadDto payload) {
        final List<ShoppingCartItemDetailedDto> items = promotion.getPromotionType()
                .filterApplicableItems(promotion, payload.getItems());

        final List<DiscountProductItemDto> discountItems = new ArrayList<>();
        final List<Double> discountedValues = new ArrayList<>(items.size());
        for (ShoppingCartItemDetailedDto item : items) {
            final DiscountProductItemDto discountItem = new DiscountProductItemDto();
            discountItem.setPercentDiscount(promotion.getDiscount());
            discountItem.setProductId(item.getProduct().getId());

            discountItems.add(discountItem);
            final double discount = MathUtil.calculatePrice(
                    MathUtil.calcPercentDiscountNoRound(
                            item.getProduct().getPrice(),
                            promotion.getDiscount()
                    ),
                    item.getQuantity(),
                    false
            );

            discountedValues.add(discount);
        }

        final double totalDiscount = MathUtil.sumAllAndRound(discountedValues.toArray(Double[]::new));
        return DiscountDtoFactory.fromPromotion(promotion, totalDiscount, discountItems);
    }
}
