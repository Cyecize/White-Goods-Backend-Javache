package com.cyecize.app.api.store.cart;

public interface ShoppingCartService {

    void removeExpiredSessions();

    String createSession();

    void removeAllItems(String sessionId);

    boolean hasItems(String sessionId);

    ShoppingCartDetailedDto getShoppingCart(String sessionId, boolean mergeFromDb);

    ShoppingCartDetailedDto addItem(String sessionId, AddShoppingCartItemDto dto);

    ShoppingCartDetailedDto removeItem(String sessionId, Long productId);

    ShoppingCartDetailedDto applyCouponCode(String sessionId, String code);

    ShoppingCartDetailedDto removeCouponCode(String sessionId);
}
