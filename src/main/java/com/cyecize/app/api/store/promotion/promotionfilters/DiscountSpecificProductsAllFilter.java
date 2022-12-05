package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.PromotionProductItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscountSpecificProductsAllFilter extends PromotionFilterBase {

    @Override
    protected boolean doTest(Promotion promotion, FilterPayloadDto payload) {
        return !this.doFilterApplicableItems(promotion, payload.getItems()).isEmpty();
    }

    @Override
    public List<ShoppingCartItemDetailedDto> doFilterApplicableItems(Promotion promotion,
            List<ShoppingCartItemDetailedDto> items) {
        final Map<Long, Integer> itemQtyMap = items.stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        ShoppingCartItemDetailedDto::getQuantity
                ));

        for (PromotionProductItem item : promotion.getProductItems()) {
            final Integer selectedQty = itemQtyMap.get(item.getProductId());
            if (selectedQty == null || selectedQty < item.getMinQuantity()) {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>(items);
    }
}
