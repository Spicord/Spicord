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
