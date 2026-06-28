package org.spicord.util;

import org.spicord.embed.Embed;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class Conversions {

    @Deprecated
    public static MessageEmbed toJdaEmbed(Embed embed) {
        return embed.toJdaEmbed();
    }
}
