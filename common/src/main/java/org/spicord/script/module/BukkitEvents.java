package org.spicord.script.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeFunction;

public class BukkitEvents {

    private List<Listener> registeredListeners = new ArrayList<>();

    private PluginManager pluginManager;
    private Plugin spicordPlugin;

    public BukkitEvents() {
        pluginManager = Bukkit.getServer().getPluginManager();
        spicordPlugin = pluginManager.getPlugin("Spicord");
    }

    public synchronized void registerEvent(Class<? extends Event> event, NativeFunction executor) {
        ListenerExecutor listener = (l, e) -> executor.call(
            Context.enter(),
            executor.getParentScope(),
            executor,
            new Object[] { event }
        );

        pluginManager.registerEvent(
            event,
            listener,
            EventPriority.NORMAL,
            listener,
            spicordPlugin
        );

        registeredListeners.add(listener);
    }

    public synchronized void unregisterAll() {
        Iterator<Listener> listeners = registeredListeners.iterator();

        while (listeners.hasNext()) {
            Listener listener = listeners.next();

            HandlerList.unregisterAll(listener);

            listeners.remove();
        }
    }

    public interface ListenerExecutor extends Listener, EventExecutor {}
}
