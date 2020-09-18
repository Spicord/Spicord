package org.spicord.player;

import eu.mcdb.universal.player.UniversalPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayer extends UniversalPlayer {

    private final ProxiedPlayer player;

    public BungeePlayer(ProxiedPlayer player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public ProxiedPlayer getProxiedPlayer() {
        return player;
    }
}
