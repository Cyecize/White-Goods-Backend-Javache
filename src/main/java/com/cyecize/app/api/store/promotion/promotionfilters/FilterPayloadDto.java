package com.cyecize.app.api.store.promotion.promotionfilters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import java.util.List;
import lombok.Data;

@Data
public class FilterPayloadDto {
    private final List<ShoppingCartItemDetailedDto> items;
    private final Double subtotal;
}
