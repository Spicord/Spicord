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
import org.spongepowered.api.text.Text;

import eu.mcdb.universal.command.UniversalCommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Wrapper class for the player object for each server type.
 * 
 * @author sheidy
 */
public class UniversalPlayer extends UniversalCommandSender {

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

    public ProxiedPlayer getProxiedPlayer() {
        return null;
    }

    public Player getBukkitPlayer() {
        return null;
    }

    public com.velocitypowered.api.proxy.Player getVelocityPlayer() {
        return null;
    }

    public org.spongepowered.api.entity.living.player.Player getSpongePlayer() {
        return null;
    }

    public boolean isProxiedPlayer() {
        return getProxiedPlayer() != null;
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
        if (isProxiedPlayer()) {
            new sendMessageBungee(getProxiedPlayer(), message);
        } else if (isBukkitPlayer()) {
            getBukkitPlayer().sendMessage(message);
        } else if (isVelocityPlayer()) {
            new sendMessageVelocity(getVelocityPlayer(), message);
        } else if (isSpongePlayer()) {
            new sendMessageSponge(getSpongePlayer(), message);
        } else {
            System.err.println("The message was not send because there's no player instance set.");
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permission == null || permission.isEmpty()) return true;

        if (isProxiedPlayer()) {
            return getProxiedPlayer().hasPermission(permission);
        } else if (isBukkitPlayer()) {
            return getBukkitPlayer().hasPermission(permission);
        } else if (isVelocityPlayer()) {
            return getVelocityPlayer().hasPermission(permission);
        } else if (isSpongePlayer()) {
            return getSpongePlayer().hasPermission(permission);
        } else {
            System.err.println("Can't check player permission because there's no player instance set.");
        }
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
        if (isProxiedPlayer()) {
            return getProxiedPlayer().isConnected();
        } else if (isBukkitPlayer()) {
            return getBukkitPlayer().isOnline();
        } else if (isVelocityPlayer()) {
            return getVelocityPlayer().isActive();
        } else if (isSpongePlayer()) {
            return getSpongePlayer().isOnline();
        } else {
            System.err.println("Can't check if the player is online because there's no player instance set.");
        }
        return false;
    }

    // this is needed to avoid ClassNotFoundException

    private static class sendMessageBungee {
        sendMessageBungee(ProxiedPlayer player, String message) {
            player.sendMessage(new TextComponent(message));
        }
    }

    private static class sendMessageVelocity {
        sendMessageVelocity(com.velocitypowered.api.proxy.Player player, String message) {
            player.sendMessage(net.kyori.text.TextComponent.of(message));
        }
    }

    private static class sendMessageSponge {
        sendMessageSponge(org.spongepowered.api.entity.living.player.Player player, String message) {
            player.sendMessage(Text.of(message));
        }
    }
}
