package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionQuery;
import com.cyecize.app.util.Page;

public interface PromotionService {

    void calculateDiscounts(PriceBag priceBag, PromotionStage stage);

    void applySingleProductDiscount(ProductDto productDto);

    PromotionDto createPromotion(CreatePromotionDto dto);

    Page<Promotion> searchPromotions(PromotionQuery query);

    void deletePromotion(Promotion promotion);

    Promotion findPromoById(Long id);

    PromotionDto editPromotion(Long promoId, CreatePromotionDto dto);
}
