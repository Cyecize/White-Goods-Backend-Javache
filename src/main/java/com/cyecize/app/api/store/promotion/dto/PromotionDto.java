package com.cyecize.app.api.store.promotion.dto;

import com.cyecize.app.api.store.promotion.DiscountType;
import com.cyecize.app.api.store.promotion.PromotionType;
import java.util.List;
import lombok.Data;

@Data
public class PromotionDto {

    private Long id;

    private String nameBg;

    private String nameEn;

    private PromotionType promotionType;

    private DiscountType discountType;

    private Long categoryId;

    private Double discount;

    private Double minSubtotal;

    private List<PromotionProductItemDto> productItems;
}
