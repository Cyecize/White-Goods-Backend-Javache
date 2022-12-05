package com.cyecize.app.util;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.ioc.annotations.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.DoubleAdder;

public class MathUtil {

    public static double calculatePrice(@Nullable Double price, @Nullable Integer qty) {
        return calculatePrice(price, qty, true);
    }

    public static double calculatePrice(
            @Nullable Double price,
            @Nullable Integer qty,
            boolean round) {
        if (price == null) {
            return 0.0;
        }

        qty = Objects.requireNonNullElse(qty, 1);

        final double res = price * qty;
        return round ? round(res) : res;
    }

    public static double calculateTotal(Collection<ShoppingCartItemDetailedDto> items) {
        final DoubleAdder da = new DoubleAdder();

        for (ShoppingCartItemDetailedDto item : items) {
            da.add(item.getCalculatedPrice());
        }

        return round(da.doubleValue());
    }

    public static double calcPercentDiscountNoRound(@Nullable Double value,
            @Nullable Double percentage) {
        value = Objects.requireNonNullElse(value, 0D);
        percentage = Objects.requireNonNullElse(percentage, 99D);
        percentage = Math.min(100D, percentage);

        return value * percentage / 100;
    }

    public static double sum(@Nullable Double n1, @Nullable Double n2) {
        n1 = Objects.requireNonNullElse(n1, 0D);
        n2 = Objects.requireNonNullElse(n2, 0D);

        return round(n1 + n2);
    }

    public static double sumAll(Double... values) {
        final DoubleAdder da = new DoubleAdder();
        for (Double value : values) {
            if (value == null) {
                continue;
            }

            da.add(value);
        }

        return round(da.sum());
    }

    public static double subtract(@Nullable Double n1, @Nullable Double n2) {
        n1 = Objects.requireNonNullElse(n1, 0D);
        n2 = Objects.requireNonNullElse(n2, 0D);

        return round(n1 - n2);
    }

    private static double round(double val) {
        final double rounded = Math.round(val * 1000) / 1000.0;
        return (int) (rounded * 100) / 100d;
    }
}
