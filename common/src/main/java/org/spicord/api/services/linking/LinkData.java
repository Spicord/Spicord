package org.spicord.api.services.linking;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

/**
 * Class containing information about the player and the discord id.
 */
public class LinkData {

    @SerializedName("id")
    private final Long discordId;

    @SerializedName("name")
    private final String playerName;

    @SerializedName("uuid")
    private final String playerId;

    public LinkData(Long discordId, String playerName, String playerId) {
        this.discordId = discordId;
        this.playerName = playerName;
        this.playerId = playerId;
    }

    /**
     * Get the player discord id
     * 
     * @return the player discord id
     */
    public long getDiscordId() {
        return discordId;
    }

    /**
     * Get the player name
     * 
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Get the player id
     * 
     * @return the player id
     */
    public UUID getPlayerId() {
        return UUID.fromString(playerId);
    }

    @Override
    public int hashCode() {
        return (discordId + playerName + playerId).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof LinkData) {
            return o.hashCode() == this.hashCode();
        }
        return false;
    }

    // OLD METHODS BELOW --

    /**
     * Use getDiscordId()
     */
    @Deprecated
    public Long getId() {
        return getDiscordId();
    }

    /**
     * Use getPlayerName()
     */
    @Deprecated
    public String getName() {
        return getPlayerName();
    }

    /**
     * Use getPlayerId()
     */
    @Deprecated
    public UUID getUniqueId() {
        return getPlayerId();
    }
}
