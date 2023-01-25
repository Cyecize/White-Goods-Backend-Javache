package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.updaters.DecreaseUpdater;
import com.cyecize.app.api.warehouse.updaters.IncreaseUpdater;
import com.cyecize.app.api.warehouse.updaters.QuantityUpdater;
import com.cyecize.app.api.warehouse.updaters.ReplaceUpdater;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QuantityUpdateType {
    INCREASE(new IncreaseUpdater()),
    DECREASE(new DecreaseUpdater()),
    ORDER_DECREASE(new DecreaseUpdater()),
    REPLACE(new ReplaceUpdater()),
    INITIAL_REPLACE(new ReplaceUpdater()),
    REVISION_REPLACE(new ReplaceUpdater());

    private final QuantityUpdater quantityUpdater;

    public static void updateQuantity(Product product, QuantityUpdate quantityUpdate) {
        if (quantityUpdate.getUpdateType() == null) {
            throw new IllegalArgumentException("Update type required to update quantity!");
        }
        quantityUpdate.getUpdateType().quantityUpdater.updateQuantity(product, quantityUpdate);
    }
}
