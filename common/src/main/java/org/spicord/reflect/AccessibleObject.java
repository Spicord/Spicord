package org.spicord.reflect;

public interface AccessibleObject<T> {

    public default T setAccessible() {
        return setAccessible(true);
    }

    T setAccessible(boolean flag);
}
