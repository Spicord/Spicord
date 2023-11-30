/*
 * Copyright (C) 2020  OopsieWoopsie
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

    public String getPlayerName() {
        return name;
    }

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
