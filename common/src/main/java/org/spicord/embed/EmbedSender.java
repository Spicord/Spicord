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
