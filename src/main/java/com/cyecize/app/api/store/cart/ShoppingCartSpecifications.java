package com.cyecize.app.api.store.cart;

import com.cyecize.app.util.Specification;

public class ShoppingCartSpecifications {

    public static Specification<ShoppingCart> userIdEquals(Long userId) {
        if (userId == null) {
            return Specification.not(null);
        }

        return (root, query, cb) -> cb.equal(root.get(ShoppingCart_.userId), userId);
    }

}
