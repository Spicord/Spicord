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

package eu.mcdb.universal.command;

import eu.mcdb.universal.player.UniversalPlayer;

public abstract class UniversalCommandSender {

    private UniversalPlayer player;

    protected void setPlayer(UniversalPlayer player) {
        this.player = player;
    }

    public String getName() {
        return isPlayer() ? player.getName() : "CONSOLE";
    }

    /**
     * Gets the player who executed the command.
     * 
     * @return the player, may be null
     * @see {@link #isPlayer()}
     */
    public UniversalPlayer getPlayer() {
        return player;
    }

    /**
     * Send a message.
     * 
     * @param message the message to be sent
     */
    public abstract void sendMessage(String message);

    /**
     * Send a formatted message, using '&' as the color char.
     * 
     * @param message the message to be sent
     */
    public void sendFormattedMessage(String message) {
        this.sendFormattedMessage(message, '&');
    }

    /**
     * Send a formatted message, using a char of your choice.
     * 
     * @param message   the message to be sent
     * @param magicChar the magic char
     */
    public void sendFormattedMessage(String message, char magicChar) {
        this.sendMessage(message.replace(magicChar, '§'));
    }

    /**
     * Checks if a sender has a permission.
     * 
     * @param permission the permission
     * @return true if the sender has the given permission
     */
    public abstract boolean hasPermission(String permission);

    /**
     * Checks if the sender is a player.
     * 
     * @return true if the sender is a player
     */
    public boolean isPlayer() {
        return player != null;
    }
}
