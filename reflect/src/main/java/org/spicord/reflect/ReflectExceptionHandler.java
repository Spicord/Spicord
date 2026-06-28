package org.spicord.reflect;

@FunctionalInterface
public interface ReflectExceptionHandler {

    void handle(Exception e);
}
