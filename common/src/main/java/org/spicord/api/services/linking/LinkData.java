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

    public long getDiscordId() {
        return discordId;
    }

    public String getPlayerName() {
        return playerName;
    }

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
