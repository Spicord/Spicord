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

package eu.mcdb.universal.player;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import eu.mcdb.universal.command.UniversalCommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Wrapper class for the player object for each server type.
 * 
 * @author sheidy
 */
public class UniversalPlayer extends UniversalCommandSender implements org.spicord.player.Player {

    private final String name;
    private final UUID uniqueId;

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

    public Object getHandle() {
        return null;
    }

    @Deprecated
    public ProxiedPlayer getProxiedPlayer() {
        return null;
    }

    @Deprecated
    public Player getBukkitPlayer() {
        return null;
    }

    @Deprecated
    public com.velocitypowered.api.proxy.Player getVelocityPlayer() {
        return null;
    }

    @Deprecated
    public ServerPlayer getSpongePlayer() {
        return null;
    }

    @Deprecated
    public boolean isProxiedPlayer() {
        return getProxiedPlayer() != null;
    }

    public boolean isBungeePlayer() {
        return isProxiedPlayer();
    }

    public boolean isBukkitPlayer() {
        return getBukkitPlayer() != null;
    }

    public boolean isVelocityPlayer() {
        return getVelocityPlayer() != null;
    }

    public boolean isSpongePlayer() {
        return getSpongePlayer() != null;
    }

    @Override
    public void sendMessage(String message) {
        System.err.println("The message was not send because there's no player instance set.");
    }

    @Override
    public boolean hasPermission(String permission) {
        System.err.println("Can't check player permission because there's no player instance set.");
        return false;
    }

    @Override
    public UniversalPlayer getPlayer() {
        return this;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        System.err.println("Can't check if the player is online because there's no player instance set.");
        return false;
    }
}
