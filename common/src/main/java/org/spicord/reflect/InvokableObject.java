package org.spicord.reflect;

public interface InvokableObject {

    <T> T invoke(Object... args);

    ReflectedObject invokeReflect(Object... args);
}
