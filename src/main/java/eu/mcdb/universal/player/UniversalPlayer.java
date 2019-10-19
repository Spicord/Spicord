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

package eu.mcdb.universal.player;

import java.util.UUID;
import org.bukkit.entity.Player;
import eu.mcdb.universal.command.UniversalCommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class UniversalPlayer extends UniversalCommandSender {

    private String name;
    private UUID uniqueId;

    public UniversalPlayer(String name, UUID uuid) {
        this.name = name;
        this.uniqueId = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ProxiedPlayer getProxiedPlayer() {
        return null;
    }

    public Player getBukkitPlayer() {
        return null;
    }

    public boolean isProxiedPlayer() {
        return getProxiedPlayer() != null;
    }

    public boolean isBukkitPlayer() {
        return getBukkitPlayer() != null;
    }

    @Override
    public void sendMessage(String message) {
        if (isProxiedPlayer()) {
            getProxiedPlayer().sendMessage(new TextComponent(message));
        } else {
            getBukkitPlayer().sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (isProxiedPlayer()) {
            return getProxiedPlayer().hasPermission(permission);
        } else {
            return getBukkitPlayer().hasPermission(permission);
        }
    }
}
