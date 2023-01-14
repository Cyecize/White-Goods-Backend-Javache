package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import com.cyecize.app.api.store.promotion.dto.DiscountsDto;
import java.util.List;

public interface PromotionService {

    DiscountsDto calculateDiscounts(List<ShoppingCartItemDetailedDto> items, Double subtotal);

    void applySingleProductDiscount(ProductDto productDto);
}
