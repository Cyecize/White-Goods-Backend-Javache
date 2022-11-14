package com.cyecize.app.api.store;

import java.util.List;

public interface ShoppingCartService {
    String createSession();

    void removeAllItems(String sessionId);

    List<ShoppingCartItemDetailedDto> getShoppingCartItems(String sessionId, boolean mergeFromDb);

    List<ShoppingCartItemDetailedDto> addItem(String sessionId, AddShoppingCartItemDto dto);

    List<ShoppingCartItemDetailedDto> removeItem(String sessionId, Long productId);
}
