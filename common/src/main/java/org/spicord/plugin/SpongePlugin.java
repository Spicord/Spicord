/*
 * Copyright (C) 2020  OopsieWoopsie
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
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginManager;
import eu.mcdb.util.SLF4JWrapper;
import lombok.Getter;

@Getter
public class SpongePlugin {

    private final static File configDir = new File("config");

    private final Game game;
    private final CommandManager commandManager;
    private final PluginManager pluginManager;
    private final EventManager eventManager;

    private File dataFolder;
    private Logger logger;

    public SpongePlugin() {
        this.game = Sponge.getGame();
        this.commandManager = game.getCommandManager();
        this.pluginManager = game.getPluginManager();
        this.eventManager = game.getEventManager();

        final Plugin plugin = getPlugin();

        this.dataFolder = new File(configDir, plugin.id());
        this.logger = new SLF4JWrapper(plugin.name().isEmpty() ? plugin.id() : plugin.name());

        this.onLoad();
        this.onEnable();
    }

    public void onLoad() {}
    public void onEnable() {}

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
