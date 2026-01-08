package com.cyecize.app.api.warehouse.updaters;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.QuantityUpdate;

public class IncreaseUpdater implements QuantityUpdater {

    @Override
    public void updateQuantity(Product product, QuantityUpdate quantityUpdate) {
        // Using Math.max to protect from integer overflow
        product.setQuantity(Math.max(product.getQuantity() + quantityUpdate.getQuantityValue(), 0));
    }
}
