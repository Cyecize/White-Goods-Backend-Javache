package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.PromotionProductItem;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscountSpecificProductsAllFilter extends PromotionFilterBase {

    @Override
    protected boolean doTest(Promotion promotion, FilterPayloadDto payload) {
        if (payload.getItems().isEmpty()) {
            return false;
        }

        final Map<Long, Integer> itemQtyMap = payload.getItems().stream()
                .collect(Collectors.toMap(
                        o -> o.getProduct().getId(),
                        ShoppingCartItemDetailedDto::getQuantity
                ));

        for (PromotionProductItem item : promotion.getProductItems()) {
            final Integer selectedQty = itemQtyMap.get(item.getProductId());
            if (selectedQty == null || selectedQty < item.getMinQuantity()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<ShoppingCartItemDetailedDto> doFilterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        final Map<Long, Integer> itemQtyMap = promotion.getProductItems().stream()
                .collect(Collectors.toMap(
                        PromotionProductItem::getProductId,
                        PromotionProductItem::getMinQuantity
                ));

        return items.stream()
                .filter(item -> {
                    final Integer minQty = itemQtyMap.get(item.getProduct().getId());
                    return minQty != null && item.getQuantity() >= minQty;
                })
                .collect(Collectors.toList());
    }
}
