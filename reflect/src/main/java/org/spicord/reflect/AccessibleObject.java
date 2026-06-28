package org.spicord.reflect;

public interface AccessibleObject<T> {

    /**
     * 
     * @return
     */
    public default T setAccessible() {
        return setAccessible(true);
    }

    /**
     * 
     * @param flag
     * @return
     */
    T setAccessible(boolean flag);
}
