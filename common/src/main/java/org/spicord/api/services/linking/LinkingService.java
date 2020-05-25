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
import org.spicord.api.services.Service;
import eu.mcdb.universal.player.UniversalPlayer;

public interface LinkingService extends Service {

    /**
     * 
     * @param player
     * @return
     */
    boolean isPending(UniversalPlayer player);

    /**
     * 
     * @param player
     * @return
     */
    boolean isLinked(UniversalPlayer player);

    /**
     * 
     * @param id
     * @return
     */
    boolean isLinked(UUID id);

    /**
     * 
     * @param id
     * @return
     */
    boolean isLinked(Long id);

    /**
     * 
     * @param data
     * @param id
     * @return
     */
    LinkData createLink(PendingLinkData data, long id);

    /**
     * 
     * @param data
     * @return
     */
    boolean removeLink(LinkData data);

    /**
     * 
     * @param data
     * @return
     */
    boolean addPending(PendingLinkData data);

    /**
     * 
     * @param data
     * @return
     */
    boolean removePending(PendingLinkData data);

    /**
     * 
     * @return
     */
    LinkData[] getLinked();

    /**
     * 
     * @return
     */
    PendingLinkData[] getPending();

    /**
     * 
     * @param name
     * @return
     */
    static boolean isValidMinecraftName(String name) {
        return name.length() >= 3
                && name.length() <= 16
                && name.replaceAll("[A-Za-z0-9_]", "").length() == 0;
    }
}
