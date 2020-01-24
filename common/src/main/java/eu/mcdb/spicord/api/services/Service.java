package eu.mcdb.spicord.api.services;

import eu.mcdb.spicord.api.Node;

public interface Service extends Node {

    String id();

    default void save() {};
    default void stop() {};
    default void reload() {};
}
