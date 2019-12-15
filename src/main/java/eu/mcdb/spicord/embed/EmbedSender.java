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

package eu.mcdb.spicord.embed;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

@UtilityClass
public class EmbedSender {

    public MessageAction prepare(TextChannel channel, Embed embed) {
        if (embed.hasEmbedData() && embed.hasContent())
            return channel.sendMessage(embed.toJdaEmbed()).append(embed.getContent());
        else if (embed.hasEmbedData())
            return channel.sendMessage(embed.toJdaEmbed());
        else if (embed.hasContent())
            return channel.sendMessage(embed.getContent());
        else
            return channel.sendMessage("empty message");
    }
}
