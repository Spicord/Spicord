package org.spicord.player;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import eu.mcdb.universal.player.UniversalPlayer;

public class SpongePlayer extends UniversalPlayer {

    private final ServerPlayer player;

    public SpongePlayer(ServerPlayer player) {
        super(player.name(), player.uniqueId());
        this.player = player;
    }

    @Override
    public ServerPlayer getSpongePlayer() {
        return player;
    }
}
