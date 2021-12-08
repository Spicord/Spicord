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

package org.spicord.api.addon;

import java.util.Map;
import java.util.Map.Entry;

import org.spicord.api.bot.command.BotCommand;
import org.spicord.bot.DiscordBot;
import org.spicord.script.ScriptEngine;

public final class JavaScriptAddon extends SimpleAddon {

    private final ScriptEngine engine;

    private final Map<String[], Object> _commands;

    private JavaScriptBaseAddon baseAddon;

    public JavaScriptAddon(String name, String id, String author, String version, JavaScriptBaseAddon addon, ScriptEngine engine) {
        super(name, id, author, version);

        this.engine = engine;
        this._commands = addon.buildCommands();
        this.baseAddon = addon;
    }

    @Override
    public void onLoad(DiscordBot bot) {
        call(baseAddon.get("load"), bot);
        setupCommands(bot);
    }

    @Override
    public void onReady(DiscordBot bot) {
        call(baseAddon.get("ready"), bot);
    }

    @Override
    public void onShutdown(DiscordBot bot) {
        call(baseAddon.get("shutdown"), bot);
    }

    @Override
    public void onDisable() {
        call(baseAddon.get("disable"));
    }

    private void setupCommands(DiscordBot bot) {
        for (final Entry<String[], Object> entry : _commands.entrySet()) {
            final String[] aliases = entry.getKey();
            final Object function = entry.getValue();

            final BotCommand exec = (command, args) -> call(function, command, args);

            for (final String alias : aliases) {
                bot.onCommand(alias, exec);
            }
        }
    }

    private void call(Object func, Object... args) {
        if (func == null) return;
        engine.callFunction(func, args);
    }
}
