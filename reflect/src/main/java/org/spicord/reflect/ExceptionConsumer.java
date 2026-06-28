package org.spicord.reflect;

@FunctionalInterface
public interface ExceptionConsumer<T> {

    T run() throws Exception;

    default T runHandled() {
        try {
            return run();
        } catch (Exception e) {}
        return null;
    }
}
