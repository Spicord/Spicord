package org.spicord.embed;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.spicord.embed.Embed.Author;
import org.spicord.embed.Embed.EmbedData;

import com.google.gson.annotations.SerializedName;

public class Webhook {

    private String username;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private String content;
    private Embed.EmbedData[] embeds;

    public Webhook(String content, Embed.EmbedData[] embeds) {
        this.content = content;
        this.embeds = embeds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public void setUserFromEmbedData() {
        for (int i = 0; i < embeds.length; i++) {
            EmbedData data = embeds[i];

            if (data.hasAuthor()) {
                Author author = data.getAuthor();
                data.removeAuthor();

                this.username = author.getName();
                this.avatarUrl = author.getIconUrl();

                return;
            }
        }
    }

    public Embed toEmbed() {
        return Embed.fromWebhook(this);
    }

    public static Webhook fromEmbed(Embed embed) {
        if (embed.hasEmbedData()) {
            final Webhook wh = new Webhook(
                embed.getContent(),
                new Embed.EmbedData[] { embed.getEmbedData() }
            );
            wh.setUserFromEmbedData();
            return wh;
        } else {
            return new Webhook(embed.getContent(), null);
        }
    }

    public void sendTo(String webhookUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(webhookUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0");
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(toJson().getBytes(StandardCharsets.UTF_8));
        out.flush();

        if (conn.getResponseCode() > 299) {
            throw new IOException("Webhook failed with response: " + conn.getResponseCode() + " " + conn.getResponseMessage());
        }

        conn.disconnect();
    }
}
