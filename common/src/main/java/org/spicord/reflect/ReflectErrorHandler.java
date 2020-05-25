package org.spicord.reflect;

@FunctionalInterface
public interface ReflectErrorHandler {

    void handle(ReflectException e);
}
