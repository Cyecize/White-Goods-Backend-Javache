package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.updaters.DecreaseUpdater;
import com.cyecize.app.api.warehouse.updaters.IncreaseUpdater;
import com.cyecize.app.api.warehouse.updaters.QuantityUpdater;
import com.cyecize.app.api.warehouse.updaters.ReplaceUpdater;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

//TODO: Consider adding 'revertable' option to increase and decrease
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QuantityUpdateType {
    INCREASE(new IncreaseUpdater(), new DecreaseUpdater()),
    DECREASE(new DecreaseUpdater(), new IncreaseUpdater()),
    ORDER_DECREASE(new DecreaseUpdater(), null),
    REPLACE(new ReplaceUpdater(), null),
    INITIAL_REPLACE(new ReplaceUpdater(), null),
    REVISION_REPLACE(new ReplaceUpdater(), null);

    private final QuantityUpdater quantityUpdater;
    private final QuantityUpdater undoUpdater;

    public static void updateQuantity(Product product, QuantityUpdate quantityUpdate) {
        if (quantityUpdate.getUpdateType() == null) {
            throw new IllegalArgumentException("Update type required to update quantity!");
        }
        quantityUpdate.getUpdateType().quantityUpdater.updateQuantity(product, quantityUpdate);
    }

    public static void applyUndo(Product product, QuantityUpdate quantityUpdate) {
        if (quantityUpdate.getUpdateType() == null) {
            throw new IllegalArgumentException("Update type required to undo update quantity!");
        }

        if (quantityUpdate.getUpdateType().undoUpdater == null) {
            throw new UnsupportedOperationException(String.format(
                    "Quantity update type %s does not support undo!",
                    quantityUpdate.getUpdateType()
            ));
        }

        quantityUpdate.getUpdateType().undoUpdater.updateQuantity(product, quantityUpdate);
    }
}
