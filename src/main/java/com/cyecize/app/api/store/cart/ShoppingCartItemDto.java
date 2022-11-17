package com.cyecize.app.api.store.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingCartItemDto {

    private final Long productId;
    private Integer quantity;
}
