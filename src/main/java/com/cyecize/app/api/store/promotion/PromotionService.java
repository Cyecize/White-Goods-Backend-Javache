package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import java.util.List;

public interface PromotionService {

    List<Promotion> getPromotions(List<ShoppingCartItemDetailedDto> items);
}
