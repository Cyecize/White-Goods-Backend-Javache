package com.cyecize.app.api.store;

import com.cyecize.app.api.product.dto.ProductDto;
import lombok.Data;

@Data
public class ShoppingCartItemDetailedDto {
    private ProductDto product;
    private Integer quantity;
}
