package eu.mcdb.spicord.embed;

import java.time.OffsetDateTime;

public class Embed {

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

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        public boolean hasUrl() {
            return url != null;
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
}
