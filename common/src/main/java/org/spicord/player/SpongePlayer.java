package org.spicord.player;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import eu.mcdb.universal.player.UniversalPlayer;
import net.kyori.adventure.text.Component;

public class SpongePlayer extends UniversalPlayer {

    private final ServerPlayer player;

    public SpongePlayer(ServerPlayer player) {
        super(player.name(), player.uniqueId());
        this.player = player;
    }

    @Override
    public Object getHandle() {
        return getSpongePlayer();
    }

    @Override
    public ServerPlayer getSpongePlayer() {
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
        return player.isOnline();
    }
}
