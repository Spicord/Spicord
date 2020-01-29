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

package eu.mcdb.spicord.api.addon;

import java.util.Map;
import java.util.Map.Entry;
import org.spicord.script.ScriptEngine;
import eu.mcdb.spicord.api.bot.command.BotCommand;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import lombok.Getter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Getter
public final class JavaScriptAddon extends SimpleAddon {

    private final ScriptEngine engine;

    private final Object load;
    private final Object ready;
    private final Map<String[], Object> _commands;

    public JavaScriptAddon(String name, String key, String author, JavaScriptBaseAddon addon, ScriptEngine engine) {
        super(name, key, author);

        this.engine = engine;
        this._commands = addon.buildCommands();
        this.load = addon.get("load");
        this.ready = addon.get("ready");
    }

    @Override
    public void onLoad(DiscordBot bot) {
        call(load, bot);
        setupCommands(bot);
    }

    @Override
    public void onReady(DiscordBot bot) {
        call(ready, bot);
    }

    @Override
    public void onCommand(DiscordBotCommand command, String[] args) {
        // will not be used
    }

    @Override
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) {}

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
