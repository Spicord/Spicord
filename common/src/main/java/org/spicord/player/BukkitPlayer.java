package org.spicord.player;

import org.bukkit.entity.Player;
import eu.mcdb.universal.player.UniversalPlayer;

public class BukkitPlayer extends UniversalPlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public Object getHandle() {
        return getBukkitPlayer();
    }

    @Override
    public Player getBukkitPlayer() {
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
        player.sendMessage(message);
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }
}
