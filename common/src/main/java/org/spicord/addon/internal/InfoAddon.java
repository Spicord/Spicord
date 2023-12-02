package org.spicord.addon.internal;

import java.awt.Color;

import org.spicord.Spicord;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.SlashCommand;

import eu.mcdb.universal.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoAddon extends SimpleAddon {

    public InfoAddon() {
        super("Server Information", "spicord::info", "Tini");
    }

    @Override
    public void onReady(DiscordBot bot) {
        SlashCommand command = bot.commandBuilder("info", "Server Information").setExecutor(this::handleCommand);
        bot.registerCommand(command);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        Server server = Server.getInstance();
        int onlineCount = server.getOnlineCount();
        int playerLimit = server.getPlayerLimit();

        StringBuilder sb = new StringBuilder();

        sb.append("Online players: ");
        sb.append(onlineCount);
        sb.append('/');

        if (playerLimit < 0) {
            sb.append("∞"); // infinity symbol
        } else {
            sb.append(playerLimit);
        }

        sb.append('\n'); // new line

        sb.append("Server version: ");
        sb.append(server.getVersion());

        final EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Server information")
                .setDescription(sb.toString())
                .setColor(new Color(5154580));

        String footer = getSpicord().getConfig().getIntegratedAddonFooter();

        if (footer == null || footer.isEmpty()) {
            // ignore
        } else {
            builder.setFooter(footer.replace("{version}", Spicord.getVersion()), null);
        }

        event.replyEmbeds(builder.build()).queue();
    }
}
