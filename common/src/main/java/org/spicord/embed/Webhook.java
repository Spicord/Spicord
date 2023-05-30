package org.spicord.embed;

public class Webhook {

    private String content;
    private Embed.EmbedData[] embeds;

    public Webhook(String content, Embed.EmbedData[] embeds) {
        this.content = content;
        this.embeds = embeds;
    }

    public String getContent() {
        return content;
    }

    public Embed.EmbedData[] getEmbeds() {
        return embeds;
    }

    public String toJson() {
        return EmbedParser.GSON.toJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }

    public Embed toEmbed() {
        return Embed.fromWebhook(this);
    }

    public static Webhook fromEmbed(Embed embed) {
        if (embed.hasEmbedData()) {
            return new Webhook(embed.getContent(), new Embed.EmbedData[] { embed.getEmbedData() });
        }
        return new Webhook(embed.getContent(), null);
    }
}
