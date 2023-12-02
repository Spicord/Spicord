package org.spicord.addon.internal;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.spicord.Spicord;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.SlashCommandBuilder;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class PlayersAddon extends SimpleAddon {

    public PlayersAddon() {
        super("Player List", "spicord::players", "Tini");
    }

    @Override
    public void onReady(DiscordBot bot) {
        SlashCommandBuilder command = bot.commandBuilder("players", "Player List")
            .addOption(OptionType.STRING, "server", "Server name", false, true)
            .setExecutor(this::handleCommand)
            .setCompleter(e -> {
                Collection<String> options = Server.getInstance()
                    .getServersAndPlayers()
                    .keySet()
                ;
                e.replyChoiceStrings(options).queue();
            })
        ;
        bot.registerCommand(command);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        Server server = Server.getInstance();

        EmbedBuilder builder = new EmbedBuilder();

        if (Server.getServerType() == ServerType.BUNGEECORD) {
            String desc = "";

            if (server.getOnlineCount() == 0) {
                event.reply(event.getUser().getAsMention() + ", there are no players online!").queue();
                return;
            }

            Map<String, List<String>> playerList = server.getServersAndPlayers();

            int playerCount = 0;

            OptionMapping serverOption = event.getOption("server");

            if (serverOption != null) {
                String serverName = serverOption.getAsString();
                List<String> players = playerList.get(serverName);

                if (players == null) {
                    String usage = "Usage: `/players [server]`";
                    event.reply(event.getUser().getAsMention() + ", the server `" + serverName + "` was not found!\n" + usage).queue();
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

            builder
                .setTitle("Total players: " + playerCount)
                .setDescription(desc)
                .setColor(new Color(5154580))
            ;
        } else {
            final String[] online = server.getOnlinePlayers();

            builder
                .setTitle("Players (" + online.length + "): ")
                .setDescription(String.join(", ", escapeUnderscores(online)))
                .setColor(new Color(5154580))
            ;
        }

        String footer = getSpicord().getConfig().getIntegratedAddonFooter();

        if (footer == null || footer.isEmpty()) {
            // ignore
        } else {
            builder.setFooter(footer.replace("{version}", Spicord.getVersion()), null);
        }

        event.replyEmbeds(builder.build()).queue();
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
