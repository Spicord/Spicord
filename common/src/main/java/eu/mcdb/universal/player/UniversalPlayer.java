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
import net.kyori.text.Component;
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
            getProxiedPlayer().sendMessage(new TextComponent(message));
        } else if (isBukkitPlayer()) {
            getBukkitPlayer().sendMessage(message);
        } else if (isVelocityPlayer()) {
            getVelocityPlayer().sendMessage(new VelocityComponent().get(message));
        } else if (isSpongePlayer()) {
            getSpongePlayer().sendMessage(new SpongeText().get(message));
        } else {
            throw new IllegalStateException("The player instance was not set");
        }
    }

    private class VelocityComponent {
        public Component get(String message) {
            return net.kyori.text.TextComponent.of(message);
        }
    }

    private class SpongeText {
        public Text get(String message) {
            return Text.of(message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (isProxiedPlayer()) {
            return getProxiedPlayer().hasPermission(permission);
        } else if (isBukkitPlayer()) {
            return getBukkitPlayer().hasPermission(permission);
        } else if (isVelocityPlayer()) {
            return getVelocityPlayer().hasPermission(permission);
        } else if (isSpongePlayer()) {
            return getSpongePlayer().hasPermission(permission);
        } else {
            throw new IllegalStateException("The player instance was not set");
        }
    }
}
