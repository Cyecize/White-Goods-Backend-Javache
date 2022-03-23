package com.cyecize.app.util;

import java.util.Arrays;

public final class ReflectionUtils {

    public static boolean fieldExists(Class<?> cls, String fieldName) {
        return Arrays.stream(cls.getDeclaredFields())
                .anyMatch(field -> field.getName().equalsIgnoreCase(fieldName));
    }
}
