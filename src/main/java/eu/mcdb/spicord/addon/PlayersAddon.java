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
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import net.dv8tion.jda.core.EmbedBuilder;

public class PlayersAddon extends SimpleAddon {

    public PlayersAddon() {
        super("Player List", "spicord::players", "OopsieWoopsie", new String[] { "players" });
    }

    @Override
    public void onCommand(DiscordBotCommand command, String[] args) {
        if (getServer().isBungeeCord()) {
            String desc = "";

            if (getServer().getOnlineCount() == 0) {
                command.reply(command.getAuthorAsMention() + ", there are no players online!");
                return;
            }

            if (args.length > 0) {
                String server = args[0].replace("`", "'");
                List<String> players = getServer().getServersAndPlayers().get(server);

                if (players == null) {
                    String usage = "Usage: `" + command.getPrefix() + "players <server>` or `" + command.getPrefix() + "players`";
                    command.reply(command.getAuthorAsMention() + ", the server `" + server + "` was not found!\n" + usage);
                    return;
                } else {
                    desc = buildServerLine(server, players);
                }
            } else {
                for (Entry<String, List<String>> entry : getServer().getServersAndPlayers().entrySet()) {
                    String server = entry.getKey();
                    List<String> players = entry.getValue();

                    String line = buildServerLine(server, players);

                    desc += line + "\n";
                }
            }

            final EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Total players: " + getServer().getOnlineCount())
                    .setDescription(desc)
                    .setColor(new Color(5154580))
                    .setFooter("Powered by Spicord v" + Spicord.getVersion(), null);

            command.reply(builder.build());
        } else {
            final EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Players (" + getServer().getOnlineCount() + "): ")
                    .setDescription(String.join(", ", escapeUnderscores(getServer().getOnlinePlayers())))
                    .setColor(new Color(5154580))
                    .setFooter("Powered by Spicord v" + Spicord.getVersion(), null);

            command.reply(builder.build());
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
