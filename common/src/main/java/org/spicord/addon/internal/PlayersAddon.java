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

package org.spicord.addon.internal;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.spicord.Spicord;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.command.DiscordBotCommand;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import net.dv8tion.jda.api.EmbedBuilder;

public class PlayersAddon extends SimpleAddon {

    public PlayersAddon() {
        super("Player List", "spicord::players", "Sheidy", new String[] { "players" });
    }

    @Override
    public void onCommand(DiscordBotCommand command, String[] args) {
        Server server = Server.getInstance();

        if (Server.getServerType() == ServerType.BUNGEECORD) {
            String desc = "";

            if (server.getOnlineCount() == 0) {
                command.reply(command.getAuthorAsMention() + ", there are no players online!");
                return;
            }

            Map<String, List<String>> playerList = server.getServersAndPlayers();

            int playerCount = 0;

            if (args.length > 0) {
                String serverName = args[0].replace("`", "'");
                List<String> players = playerList.get(serverName);

                if (players == null) {
                    String usage = "Usage: `" + command.getPrefix() + "players [server]`";
                    command.reply(command.getAuthorAsMention() + ", the server `" + serverName + "` was not found!\n" + usage);
                    return;
                } else {
                    desc = buildServerLine(serverName, players);
                    playerCount = players.size();
                }
            } else {
                for (Entry<String, List<String>> entry : playerList.entrySet()) {
                    String serverName = entry.getKey();
                    List<String> players = entry.getValue();
                    playerCount += players.size();

                    String line = buildServerLine(serverName, players);

                    desc += line + "\n";
                }
            }

            final EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Total players: " + playerCount)
                    .setDescription(desc)
                    .setColor(new Color(5154580));

            setFooter(builder);

            command.reply(builder.build());
        } else {
            final String[] online = server.getOnlinePlayers();

            final EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Players (" + online.length + "): ")
                    .setDescription(String.join(", ", escapeUnderscores(online)))
                    .setColor(new Color(5154580));

            setFooter(builder);

            command.reply(builder.build());
        }
    }

    private void setFooter(EmbedBuilder builder) {
        String footer = getSpicord().getConfig().getIntegratedAddonFooter();

        if (footer == null || footer.isEmpty()) {
            // ignore
        } else {
            builder.setFooter(footer.replace("{version}", Spicord.getVersion()), null);
        }
    }

    private String buildServerLine(String server, List<String> players) {
        String line = "[" + server + "] (" + players.size() + "): ";
        line += String.join(", ", escapeUnderscores(players));
        return line;
    }

    private String[] escapeUnderscores(String... players) {
        return escapeUnderscores(Arrays.asList(players));
    }

    private String[] escapeUnderscores(List<String> players) {
        return players.stream()
                .map(s -> s.replace("_", "\\_"))
                .toArray(String[]::new);
    }
}
