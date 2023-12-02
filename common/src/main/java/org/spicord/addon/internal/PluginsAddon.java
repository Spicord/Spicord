package org.spicord.addon.internal;

import java.awt.Color;

import org.spicord.Spicord;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.SlashCommandBuilder;

import eu.mcdb.universal.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PluginsAddon extends SimpleAddon {

    public PluginsAddon() {
        super("Plugin List", "spicord::plugins", "Tini");
    }

    @Override
    public void onReady(DiscordBot bot) {
        SlashCommandBuilder command = bot.commandBuilder("plugins", "Plugin List")
            .setExecutor(this::handleCommand)
        ;
        bot.registerCommand(command);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        final Server server = Server.getInstance();

        final EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Plugins (" + server.getPlugins().length + "): ")
                .setDescription(String.join(", ", server.getPlugins()))
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
