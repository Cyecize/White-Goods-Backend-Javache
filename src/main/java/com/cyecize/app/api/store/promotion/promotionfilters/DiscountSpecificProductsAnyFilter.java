package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.PromotionProductItem;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscountSpecificProductsAnyFilter extends PromotionFilterBase {

    @Override
    protected boolean doTest(Promotion promotion, PriceBag priceBag) {
        final Map<Long, Integer> prodQtyMap = promotion.getProductItems()
                .stream()
                .collect(Collectors.toMap(
                        PromotionProductItem::getProductId, PromotionProductItem::getMinQuantity
                ));

        for (ShoppingCartItemDetailedDto item : priceBag.getShoppingCart().getItems()) {
            final Integer minQty = prodQtyMap.get(item.getProduct().getId());
            if (minQty == null) {
                continue;
            }

            if (minQty <= item.getQuantity()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ShoppingCartItemDetailedDto> doFilterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        final Map<Long, Integer> prodQtyMap = promotion.getProductItems()
                .stream()
                .collect(Collectors.toMap(
                        PromotionProductItem::getProductId, PromotionProductItem::getMinQuantity
                ));

        return items.stream()
                .filter(item -> {
                    final Integer minQty = prodQtyMap.get(item.getProduct().getId());
                    if (minQty == null) {
                        return false;
                    }

                    return minQty <= item.getQuantity();
                })
                .collect(Collectors.toList());
    }
}
