package org.spicord.player;

import org.spongepowered.api.entity.living.player.Player;

import eu.mcdb.universal.player.UniversalPlayer;

public class SpongePlayer extends UniversalPlayer {

    private final Player player;

    public SpongePlayer(Player player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public Player getSpongePlayer() {
        return player;
    }
}
