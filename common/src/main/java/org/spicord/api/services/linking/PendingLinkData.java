package org.spicord.api.services.linking;

import java.util.UUID;

import eu.mcdb.universal.player.UniversalPlayer;

/**
 * Class containing information about the player.
 */
public class PendingLinkData {

    private final String name;
    private final UUID uniqueId;

    public PendingLinkData(String playerName, UUID playerId) {
        this.name = playerName;
        this.uniqueId = playerId;
    }

    /**
     * Get the player name
     * 
     * @return the player name
     */
    public String getPlayerName() {
        return name;
    }

    /**
     * Get the player id
     * 
     * @return the player id
     */
    public UUID getPlayerId() {
        return uniqueId;
    }

    public LinkData complete(long discordId) {
        return new LinkData(discordId, name, uniqueId.toString());
    }

    public static PendingLinkData forPlayer(UniversalPlayer player) {
        return new PendingLinkData(player.getName(), player.getUniqueId());
    }

    // OLD METHODS BELOW --

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

    /**
     * Use complete(id)
     */
    @Deprecated
    public LinkData create(long discordId) {
        return complete(discordId);
    }

    /**
     * Use forPlayer(player)
     */
    @Deprecated
    public static PendingLinkData of(UniversalPlayer player) {
        return forPlayer(player);
    }
}
