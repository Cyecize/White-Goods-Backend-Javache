package com.cyecize.app.api.store;

import lombok.Data;

@Data
public class ShoppingCartItemDto {

    private Long productId;
    private Integer quantity;
}
