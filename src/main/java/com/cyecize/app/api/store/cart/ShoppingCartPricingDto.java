package com.cyecize.app.api.store.cart;

import lombok.Data;

@Data
public class ShoppingCartPricingDto {
    private final Double subtotal;
    private final Double deliveryPrice;
    private final Double total;
}
