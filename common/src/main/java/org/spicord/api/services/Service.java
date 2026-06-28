package org.spicord.api.services;

public interface Service {

    String id();

    default void load() {};
    default void stop() {};
    default void reload() {};
}
