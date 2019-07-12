package eu.mcdb.spicord.util;

import eu.mcdb.spicord.embed.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Conversions {

    public static MessageEmbed toJdaEmbed(Embed embed) {
        EmbedBuilder em = new EmbedBuilder();
        if (embed.hasEmbedData()) {
            Embed.EmbedData data = embed.getEmbedData();
            em.setColor(data.getColor());
            if (data.hasTitle() && data.hasUrl())
                em.setTitle(data.getTitle(), data.getUrl());
            else if (data.hasTitle())
                em.setTitle(data.getTitle());
            if (data.hasDescription())
                em.appendDescription(data.getDescription());
            if (data.hasImage())
                em.setImage(data.getImageUrl());
            if (data.hasThumbnail())
                em.setThumbnail(data.getThumbnailUrl());
            if (data.hasTimestamp())
                em.setTimestamp(data.getTimestamp());
            if (data.hasAuthor()) {
                Embed.Author au = data.getAuthor();
                if (au.hasIconUrl())
                    em.setAuthor(au.getName(), au.getUrl(), au.getIconUrl());
                else if (au.hasUrl())
                    em.setAuthor(au.getName(), au.getUrl());
                else
                    em.setAuthor(au.getName());
            }
            if (data.hasFields()) {
                for (Embed.Field field : data.getFields())
                    em.addField(field.getName(), field.getValue(), field.isInline());
            }
            if (data.hasFooter()) {
                Embed.Footer footer = data.getFooter();
                em.setFooter(footer.getText(), footer.getIconUrl());
            }
        }
        return em.build();
    }
}
