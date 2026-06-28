package org.spicord.reflect;

public interface InvokableObject {

    /**
     * 
     * @param <T>
     * @param args
     * @return
     */
    <T> T invoke(Object... args);

    /**
     * 
     * @param args
     * @return
     */
    ReflectedObject invokeReflect(Object... args);

}
