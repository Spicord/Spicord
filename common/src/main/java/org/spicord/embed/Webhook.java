package org.spicord.embed;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public Embed toEmbed() {
        return Embed.fromWebhook(this);
    }

    public static Webhook fromEmbed(Embed embed) {
        if (embed.hasEmbedData()) {
            return new Webhook(embed.getContent(), new Embed.EmbedData[] { embed.getEmbedData() });
        }
        return new Webhook(embed.getContent(), null);
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
