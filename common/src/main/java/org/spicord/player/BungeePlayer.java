package org.spicord.player;

import eu.mcdb.universal.player.UniversalPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayer extends UniversalPlayer {

    private final ProxiedPlayer player;

    public BungeePlayer(ProxiedPlayer player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public Object getHandle() {
        return getProxiedPlayer();
    }

    @Override
    public ProxiedPlayer getProxiedPlayer() {
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
        player.sendMessage(new TextComponent(message));
    }

    @Override
    public boolean isOnline() {
        return player.isConnected();
    }
}
