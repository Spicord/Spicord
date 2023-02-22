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

package org.spicord.embed;

import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class EmbedSender {

    public static MessageCreateAction prepare(GuildMessageChannel channel, Embed embed) {
        if (embed.hasEmbedData() && embed.hasContent())
            return channel.sendMessageEmbeds(embed.toJdaEmbed()).addContent(embed.getContent());
        else if (embed.hasEmbedData())
            return channel.sendMessageEmbeds(embed.toJdaEmbed());
        else if (embed.hasContent())
            return channel.sendMessage(embed.getContent());
        else
            return channel.sendMessage("empty message");
    }
}
