package com.cyecize.app.api.store.cart;

import com.cyecize.app.api.store.promotion.dto.DiscountsDto;
import lombok.Data;

@Data
public class ShoppingCartPricingDto {

    private final Double subtotal;
    private final Double deliveryPrice;
    private final Double total;
    private final DiscountsDto discounts;
}
