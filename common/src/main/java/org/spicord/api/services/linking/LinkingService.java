package org.spicord.api.services.linking;

import java.util.UUID;
import org.spicord.api.services.Service;
import eu.mcdb.universal.player.UniversalPlayer;

public interface LinkingService extends Service {

    /**
     * Check if the given player is pending to link their account.
     * 
     * @param player the player
     * @return true if the player is pending to link
     */
    boolean isPending(UniversalPlayer player);

    /**
     * Check if the given player has linked their account.
     * 
     * @param player the player
     * @return true if the player is linked
     */
    boolean isLinked(UniversalPlayer player);

    /**
     * Check if a player with the given id has linked their account.
     * 
     * @param playerId the id of a player
     * @return true if the player is linked
     */
    boolean isLinked(UUID playerId);

    /**
     * Check if the given Discord user id is linked to a player.
     * 
     * @param discordId the discord id
     * @return true if the user is linked
     */
    boolean isLinked(Long discordId);

    /**
     * Link the player with a discord id and return the LinkData instance.
     * 
     * @param playerData the player data
     * @param discordId the discord user id
     * @return the resulting LinkData
     */
    LinkData createLink(PendingLinkData playerData, long discordId);

    /**
     * Remove the given link data.
     * 
     * @param data the data to remove
     * @return true if the operation succeeded
     */
    boolean removeLink(LinkData data);

    /**
     * Add the given pending link data.
     * 
     * @param data the data to add
     * @return true if the operation succeeded
     */
    boolean addPending(PendingLinkData data);

    /**
     * Remove the given pending link data.
     * 
     * @param data the data to remove
     * @return true if the operation succeeded
     */
    boolean removePending(PendingLinkData data);

    /**
     * Get all the link data
     * 
     * @return an array containing all the link data
     */
    LinkData[] getLinked();

    /**
     * Get all the pending link data
     * 
     * @return an array containing all the pending link data
     */
    PendingLinkData[] getPending();

    /**
     * Get the link data from a discord id
     * 
     * @param discordId the player discord id
     * @return
     */
    LinkData getLinkData(Long discordId);

    /**
     * Get the link data from a player id
     * 
     * @param playerId the player id
     * @return
     */
    LinkData getLinkData(UUID playerId);

    /**
     * Check if the given player name is a valid "Minecraft: Java Edition" name.
     * 
     * @param name the player name
     * @return true if the name is a valid 
     */
    public static boolean isValidMinecraftName(String name) {
        return name.length() >= 3
                && name.length() <= 16
                && name.replaceAll("[A-Za-z0-9_]", "").length() == 0;
    }
}
