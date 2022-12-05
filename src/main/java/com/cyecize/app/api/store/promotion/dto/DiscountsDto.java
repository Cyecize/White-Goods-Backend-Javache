package com.cyecize.app.api.store.promotion.dto;

import java.util.List;
import lombok.Data;

@Data
public class DiscountsDto {

    private final boolean freeDelivery;
    private final Double totalDiscounts;
    private final List<DiscountDto> promotions;
}
