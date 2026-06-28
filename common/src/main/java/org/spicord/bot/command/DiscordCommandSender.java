package org.spicord.bot.command;

import eu.mcdb.universal.command.UniversalCommandSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public class DiscordCommandSender extends UniversalCommandSender {

    protected final Member member;
    protected final GuildMessageChannel channel;
    private PrivateChannel _channel;

    public DiscordCommandSender(final DiscordBotCommand command) {
        this.member = command.getSender();
        this.channel = command.getChannel();
    }

    @Override
    public String getName() {
        return member.getEffectiveName();
    }

    @Override
    public void sendMessage(final String message) {
        if (_channel == null)
            _channel = member.getUser().openPrivateChannel().complete();

        channel.sendMessage(message).queue();
    }

    @Override
    public boolean hasPermission(final String permission) {
        if (permission == null) return true;
        final Permission perm = Permission.valueOf(permission.toUpperCase());
        return perm != null && member.hasPermission(perm);
    }
}
