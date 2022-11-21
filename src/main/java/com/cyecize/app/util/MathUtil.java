package com.cyecize.app.util;

import com.cyecize.ioc.annotations.Nullable;
import java.util.Objects;

public class MathUtil {

    public static double calculatePrice(@Nullable Double price, @Nullable Integer qty) {
        if (price == null) {
            return 0.0;
        }

        qty = Objects.requireNonNullElse(qty, 1);

        return Math.round(price * qty * 100.0) / 100.0;
    }
}
