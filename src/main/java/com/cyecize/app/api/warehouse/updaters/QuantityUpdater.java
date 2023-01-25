package com.cyecize.app.api.warehouse.updaters;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.QuantityUpdate;

public interface QuantityUpdater {

    void updateQuantity(Product product, QuantityUpdate quantityUpdate);
}
