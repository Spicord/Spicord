package org.spicord.bungee.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.md_5.bungee.api.plugin.Event;

public class BungeeEventProcessor {

    private final Map<Class<?>, Set<Consumer<?>>> allHandlers = new HashMap<>();

    public synchronized <T extends Event> Runnable registerEvent(Class<T> event, Consumer<T> handler) {
        Set<Consumer<?>> handlers = allHandlers.computeIfAbsent(event, e -> new HashSet<>());

        handlers.add(handler);

        return () -> handlers.remove(handler);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public synchronized void handle(Event event) {
        Set<Consumer<?>> handlers = allHandlers.get(event.getClass());

        if (handlers != null) {
            for (Consumer c : handlers) {
                c.accept(event);
            }
        }
    }
}
