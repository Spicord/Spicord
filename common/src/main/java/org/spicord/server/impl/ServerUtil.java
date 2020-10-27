package org.spicord.server.impl;

import com.velocitypowered.api.proxy.ProxyServer;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;

public class ServerUtil {

    public static Server buildServer(ServerType serverType) {
        switch (serverType) {
        case BUKKIT:
            return new BukkitServer();
        case BUNGEECORD:
            return new BungeeServer();
        case SPONGE:
            return new SpongeServer();
        case VELOCITY:
            return new VelocityServer();
        default:
            return new DummyServer();
        }
    }    

    public static void setVelocityHandle(Object proxy) {
        VelocityServer.setHandle((ProxyServer) proxy);
    }
}
