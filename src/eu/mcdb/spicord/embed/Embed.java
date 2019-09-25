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

import java.io.Serializable;
import java.time.OffsetDateTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public class Embed implements Serializable {

    private static final long serialVersionUID = 1L;

    public Embed() {
    }

    private Embed(String content) {
        this.content = content;
    }

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

    public class EmbedData {

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
             return timestamp == null ? OffsetDateTime.now() : OffsetDateTime.parse(timestamp);
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
    }

    public class Footer {

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

    public class Thumbnail {

        private String url;
    }

    public class Image {

        private String url;
    }

    public class Author {

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

    public class Field {

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
     * @param json the json to be parsed.
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
                .replace("{author:name}", user.getName())
                .replace("{author:id}", user.getId())
                .replace("{author:avatar_url}", user.getAvatarUrl())
                .replace("{author:discriminator}", user.getDiscriminator())
                .replace("{author:mention}", user.getAsMention());
        return fromJson(json);
    }

    @Override
    public Embed clone() {
        // planning to make this manual but im too busy for write a lot of code.
        // probably i will do this later, idk
        return fromJson(toString());
    }
}
