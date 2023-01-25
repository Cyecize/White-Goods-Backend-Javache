package com.cyecize.app.api.warehouse.updaters;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.QuantityUpdate;

public class ReplaceUpdater implements QuantityUpdater {

    @Override
    public void updateQuantity(Product product, QuantityUpdate quantityUpdate) {
        // Use Math.max to avoid case the value is negative
        product.setQuantity(Math.max(quantityUpdate.getQuantityValue(), 0));
    }
}
