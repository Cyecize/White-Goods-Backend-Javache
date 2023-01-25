package com.cyecize.app.api.warehouse.updaters;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.QuantityUpdate;

public class DecreaseUpdater implements QuantityUpdater {
    @Override
    public void updateQuantity(Product product, QuantityUpdate quantityUpdate) {
        product.setQuantity(Math.max(product.getQuantity() - quantityUpdate.getQuantityValue(), 0));
    }
}
