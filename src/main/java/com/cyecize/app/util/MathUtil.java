package com.cyecize.app.util;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.ioc.annotations.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.DoubleAdder;

public class MathUtil {

    public static double calculatePrice(@Nullable Double price, @Nullable Integer qty) {
        if (price == null) {
            return 0.0;
        }

        qty = Objects.requireNonNullElse(qty, 1);

        return round(price * qty);
    }

    public static double calculateTotal(Collection<ShoppingCartItemDetailedDto> items) {
        final DoubleAdder da = new DoubleAdder();

        for (ShoppingCartItemDetailedDto item : items) {
            da.add(item.getCalculatedPrice());
        }

        return round(da.doubleValue());
    }

    public static double sum(@Nullable Double n1, @Nullable Double n2) {
        n1 = Objects.requireNonNullElse(n1, 0D);
        n2 = Objects.requireNonNullElse(n2, 0D);

        return round(n1 + n2);
    }

    private static double round(double val) {
        return Math.round(val * 100) / 100.0;
    }
}
