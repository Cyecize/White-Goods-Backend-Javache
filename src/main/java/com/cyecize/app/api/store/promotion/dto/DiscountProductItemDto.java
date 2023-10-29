package com.cyecize.app.api.store.promotion.dto;

import com.cyecize.app.api.store.promotion.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountProductItemDto {

    private Long productId;
    private Double discountValue;
    private DiscountType discountType;
}
