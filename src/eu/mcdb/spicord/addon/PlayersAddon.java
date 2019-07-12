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

package eu.mcdb.spicord.addon;

import java.awt.Color;
import java.util.stream.Stream;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import eu.mcdb.util.Server;
import net.dv8tion.jda.core.EmbedBuilder;

public class PlayersAddon extends SimpleAddon {

    public PlayersAddon() {
        super("Player List", "spicord::players", "OopsieWoopsie");
    }

    @Override
    public void onLoad(DiscordBot bot) {
        bot.onCommand("players", this::playersCommand);
    }

    private void playersCommand(DiscordBotCommand command) {
        command.getMessage().getChannel()
                .sendMessage(new EmbedBuilder().setTitle("Players (" + Server.getOnlineCount() + "): ")
                        .setDescription(String.join(", ", escapeUnderscores(Server.getOnlinePlayers())))
                        .setColor(new Color(5154580)).setFooter("Powered by Spicord", null).build())
                .queue();
    }

    private String[] escapeUnderscores(String[] strings) {
        return Stream.of(strings).map(s -> s.replace("_", "\\_")).toArray(String[]::new);
    }
}
