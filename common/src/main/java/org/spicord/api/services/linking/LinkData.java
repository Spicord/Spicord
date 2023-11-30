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

/**
 * Class containing information about the player and the discord id.
 */
public class LinkData {

    private final Long id;
    private final String name;
    private final String uuid;

    public LinkData(Long discordId, String playerName, String playerId) {
        this.id = discordId;
        this.name = playerName;
        this.uuid = playerId;
    }

    public long getDiscordId() {
        return id;
    }

    public String getPlayerName() {
        return name;
    }

    public UUID getPlayerId() {
        return UUID.fromString(uuid);
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
