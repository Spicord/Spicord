/*
 * Copyright (C) 2019  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord.plugin;

import java.io.File;
import java.util.logging.Logger;
import org.spicord.server.impl.ServerUtil;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.mcdb.util.SLF4JWrapper;
import lombok.Getter;
import lombok.NonNull;

public abstract class VelocityPlugin {

    private final static File pluginsDir = new File("plugins");

    @Getter private static ProxyServer proxyServer;
    @Getter private static CommandManager commandManager;
    @Getter private static EventManager eventManager;
    @Getter private static PluginManager pluginManager;

    @Getter private File dataFolder;
    @Getter private Logger logger;

    public VelocityPlugin() {
        check();
    }

    public VelocityPlugin(@NonNull ProxyServer server) {
        proxyServer    = server;
        commandManager = server.getCommandManager();
        eventManager   = server.getEventManager();
        pluginManager  = server.getPluginManager();
        ServerUtil.setVelocityHandle(server);

        check();
    }

    public void onLoad() {}
    public void onEnable() {}

    private void check() {
        final Plugin name = getPlugin();

        this.dataFolder = new File(pluginsDir, name.id());
        this.logger = new SLF4JWrapper(name.name().isEmpty() ? name.id() : name.name());

        this.onLoad();
        this.onEnable(); // TODO
    }

    private Plugin getPlugin() {
        final Class<?> clazz = getClass();

        if (clazz.isAnnotationPresent(Plugin.class)) {
            return clazz.getAnnotation(Plugin.class);
        }

        throw new IllegalStateException(String.format("missing annotation %s for class %s", Plugin.class.getName(), clazz.getName()));
    }

    public final org.slf4j.Logger getSLF4JLogger() {
        return ((SLF4JWrapper) logger).getSLF4JLogger();
    }
}
