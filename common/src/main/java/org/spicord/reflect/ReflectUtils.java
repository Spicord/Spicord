package org.spicord.reflect;

import java.lang.reflect.Array;

public final class ReflectUtils {

    public static Class<?> getArrayClass(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

    public static <T> T nullOnException(ExceptionConsumer<T> action) {
        return action.runHandled();
    }
}
