package com.cyecize.app.api.store.promotion.dto;

import lombok.Data;

@Data
public class DiscountProductItemDto {

    private Long productId;
    private Double percentDiscount;
}
