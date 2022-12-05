package com.cyecize.app.api.store.promotion.discounters;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import java.util.List;
import lombok.Data;

@Data
public class DiscounterPayloadDto {

    private final List<ShoppingCartItemDetailedDto> items;
}
