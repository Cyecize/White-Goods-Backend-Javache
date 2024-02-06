package com.cyecize.app.api.store.pricing;

import com.cyecize.app.api.store.cart.ShoppingCartDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartService;
import com.cyecize.app.api.store.promotion.PromotionService;
import com.cyecize.app.api.store.promotion.PromotionStage;
import com.cyecize.app.util.MathUtil;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final ShoppingCartService shoppingCartService;
    private final PromotionService promotionService;

    @Configuration("delivery.price")
    private final Double deliveryPrice;

    @Override
    public Price getPrice(String shoppingCartId) {
        final ShoppingCartDetailedDto shoppingCart = this.shoppingCartService
                .getShoppingCart(shoppingCartId, false);
        final Double subtotal = MathUtil.calculateTotal(shoppingCart.getItems());

        final PriceBag priceBag = new PriceBag(shoppingCart, subtotal);
        this.promotionService.calculateDiscounts(priceBag, PromotionStage.REGULAR);

        this.setTotal(priceBag);
        this.promotionService.calculateDiscounts(priceBag, PromotionStage.ADDITIONAL);

        this.setTotal(priceBag);

        return new Price(
                subtotal,
                deliveryPrice,
                priceBag.getTotal(),
                priceBag.isFreeDelivery(),
                priceBag.sumAllDiscounts(),
                priceBag.getAllDiscounts()
        );
    }

    private void setTotal(PriceBag priceBag) {
        final double deliveryPrice = priceBag.isFreeDelivery() ? 0D : this.deliveryPrice;
        final double total = Math.max(
                0D,
                MathUtil.subtract(
                        MathUtil.sum(priceBag.getSubtotal(), deliveryPrice),
                        priceBag.sumAllDiscounts()
                )
        );

        priceBag.setTotal(MathUtil.round(total));
    }
}
