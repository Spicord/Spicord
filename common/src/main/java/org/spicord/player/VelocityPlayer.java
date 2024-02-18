package org.spicord.player;

import com.velocitypowered.api.proxy.Player;

import eu.mcdb.universal.player.UniversalPlayer;
import net.kyori.adventure.text.Component;

public class VelocityPlayer extends UniversalPlayer {

    private final Player player;

    public VelocityPlayer(Player player) {
        super(player.getUsername(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public Object getHandle() {
        return getVelocityPlayer();
    }

    @Override
    public Player getVelocityPlayer() {
        return player;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Component.text(message));
    }

    @Override
    public boolean isOnline() {
        return player.isActive();
    }
}
