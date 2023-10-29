package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.pricing.PriceBag;

public interface PromotionService {

    void calculateDiscounts(PriceBag priceBag, PromotionStage stage);

    void applySingleProductDiscount(ProductDto productDto);
}
