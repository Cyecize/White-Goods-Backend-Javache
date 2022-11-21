package com.cyecize.app.api.store.cart;

import com.cyecize.app.api.product.dto.ProductDto;
import lombok.Data;

@Data
public class ShoppingCartItemDetailedDto {
    private final ProductDto product;
    private final Integer quantity;
    private final Double calculatedPrice;
}
