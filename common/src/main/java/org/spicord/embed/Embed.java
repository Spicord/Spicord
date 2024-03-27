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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.OffsetDateTime;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public class Embed implements Serializable {

    private static final long serialVersionUID = 1L;

    private Embed() {
    }

    public Embed(String description) {
        this.embed = new EmbedData();
        this.embed.description = description;
    }

    public Embed(String title, String description) {
        this.embed = new EmbedData();
        this.embed.title = title;
        this.embed.description = description;
    }

    private Integer __version;
    private String content;
    private EmbedData embed;

    /**
     * @return true if the message has content
     */
    public boolean hasContent() {
        return content != null;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return true if the message has embed data
     */
    public boolean hasEmbedData() {
        return embed != null;
    }

    /**
     * @return the embed data
     */
    public EmbedData getEmbedData() {
        return embed;
    }

    public static class EmbedData implements Serializable {

        private static final long serialVersionUID = 1L;

        private String title;
        private String description;
        private String url;
        private Integer color = 0x1FFFFFFF;
        private String timestamp;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;
        private Field[] fields;

        public boolean hasTitle() {
            return title != null;
        }

        public boolean hasDescription() {
            return description != null;
        }

        public boolean hasUrl() {
            return url != null;
        }

        public boolean hasThumbnail() {
            return thumbnail != null;
        }

        public boolean hasTimestamp() {
            return timestamp != null;
        }

        public boolean hasImage() {
            return image != null;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @return the color
         */
        public Integer getColor() {
            return color;
        }

        /**
         * @return the timestamp
         */
        public OffsetDateTime getTimestamp() {
            return (timestamp == null || "0000-00-00T00:00:00".equals(timestamp))
                ? OffsetDateTime.now()
                : OffsetDateTime.parse(timestamp);
        }

        /**
         * @return true if the embed has a footer
         */
        public boolean hasFooter() {
            return footer != null;
        }

        /**
         * @return the footer
         */
        public Footer getFooter() {
            return footer;
        }

        /**
         * @return the thumbnail url
         */
        public String getThumbnailUrl() {
            return thumbnail.url;
        }

        /**
         * @return the image url
         */
        public String getImageUrl() {
            return image.url;
        }

        /**
         * @return true if the embed has an author
         */
        public boolean hasAuthor() {
            return author != null;
        }

        /**
         * @return the author
         */
        public Author getAuthor() {
            return author;
        }

        /**
         * @return true if the embed has fields
         */
        public boolean hasFields() {
            return fields != null;
        }

        /**
         * @return the fields
         */
        public Field[] getFields() {
            return fields;
        }

        public void removeAuthor() {
            author = null;
        }
    }

    public class Footer implements Serializable {

        private static final long serialVersionUID = 1L;

        private String icon_url;
        private String text;

        /**
         * @return the icon url
         */
        public String getIconUrl() {
            return icon_url;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }
    }

    public class Thumbnail implements Serializable {

        private static final long serialVersionUID = 1L;

        private String url;
    }

    public class Image implements Serializable {

        private static final long serialVersionUID = 1L;

        private String url;
    }

    public class Author implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;
        private String url;
        private String icon_url;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        public boolean hasUrl() {
            return url != null;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @return the icon url
         */
        public String getIconUrl() {
            return icon_url;
        }

        public boolean hasIconUrl() {
            return icon_url != null;
        }
    }

    public class Field implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;
        private String value;
        private Boolean inline;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @return the inline flag
         */
        public Boolean isInline() {
            return inline == null ? false : inline;
        }
    }

    /**
     * Converts a json string to a {@link Embed} object.
     * 
     * @param json the json to be parsed.
     * @return the {@link Embed} object.
     */
    public static Embed fromJson(String json) {
        return EmbedParser.parse(json);
    }

    /**
     * Converts a string to a {@link Embed} object.
     * 
     * @param content the embed content
     * @return the {@link Embed} object.
     */
    public static Embed fromString(String content) {
        return new Embed(content);
    }

    /**
     * Converts this object to a json string.
     * 
     * @return the json string.
     */
    public String toJson() {
        return EmbedParser.GSON.toJson(this);
    }

    /**
     * Converts this object to a json string.
     * 
     * @return the json string.
     */
    @Override
    public String toString() {
        return toJson();
    }

    /**
     * Creates an embed instance compatible with JDA.
     * 
     * @return a JDA embed representing this instance
     */
    public MessageEmbed toJdaEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        if (hasEmbedData()) {
            EmbedData data = getEmbedData();
            builder.setColor(data.getColor());
            if (data.hasTitle() && data.hasUrl())
                builder.setTitle(data.getTitle(), data.getUrl());
            else if (data.hasTitle())
                builder.setTitle(data.getTitle());
            if (data.hasDescription())
                builder.appendDescription(data.getDescription());
            if (data.hasImage())
                builder.setImage(data.getImageUrl());
            if (data.hasThumbnail())
                builder.setThumbnail(data.getThumbnailUrl());
            if (data.hasTimestamp())
                builder.setTimestamp(data.getTimestamp());
            if (data.hasAuthor()) {
                Author author = data.getAuthor();
                if (author.hasIconUrl())
                    builder.setAuthor(author.getName(), author.getUrl(), author.getIconUrl());
                else if (author.hasUrl())
                    builder.setAuthor(author.getName(), author.getUrl());
                else
                    builder.setAuthor(author.getName());
            }
            if (data.hasFields()) {
                for (Field field : data.getFields())
                    builder.addField(field.getName(), field.getValue(), field.isInline());
            }
            if (data.hasFooter()) {
                Footer footer = data.getFooter();
                builder.setFooter(footer.getText(), footer.getIconUrl());
            }
        }
        return builder.build();
    }

    public Embed setPlaceholders(User user) {
        String json = toString()
                .replace("{user:name}", user.getName())
                .replace("{user:id}", user.getId())
                .replace("{user:avatar}", user.getAvatarUrl())
                .replace("{user:discriminator}", user.getDiscriminator())
                .replace("{user:mention}", user.getAsMention());

        Embed temp = fromJson(json);
        this.content = temp.content;
        this.embed = temp.embed;

        return this;
    }

    /**
     * Serializes the data of this object instance.
     * 
     * @return the serialized embed
     * @throws IOException if an I/O error occurs while writing stream header
     */
    public byte[] serialize() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    public Embed unserialize(byte[] data) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Embed) ois.readObject();
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Clones this instance.
     */
    @Override
    public Embed clone() {
        return fromJson(toJson());
    }

    /**
     * Sends this embed message to a text channel.
     * 
     * @param channel the channel
     * @return the response value
     */
    public Message sendToChannel(GuildMessageChannel channel) {
        return EmbedSender.prepare(channel, this).complete();
    }

    public void sendToChannelAsync(GuildMessageChannel channel) {
        EmbedSender.prepare(channel, this).queue();
    }

    public Webhook toWebhook() {
        return Webhook.fromEmbed(this);
    }

    public static Embed fromWebhook(Webhook webhook) {
        Embed embed = new Embed();
        embed.content = webhook.getContent();
        if (webhook.getEmbeds() != null && webhook.getEmbeds().length > 0) {
            embed.embed = webhook.getEmbeds()[0];
        } else {
            embed.embed = new EmbedData();
        }
        return embed;
    }

    /**
     * Gets the embed version used to know if the embed
     * is just a single json or if some values needs to be
     * evaluated.
     * 
     * @return the embed version number
     */
    public int getVersion() {
        return __version == null ? 0 : __version;
    }
}
