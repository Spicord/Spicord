package org.spicord.util;

import org.bukkit.entity.Player;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class VanishAPI {

    private static VanishAPI self;

    public static VanishAPI get() {
        if (self == null) {
            if (isPresent()) {
                if (Server.getServerType() == ServerType.BUNGEECORD) {
                    return self = new Bungee();
                } else if (Server.getServerType() == ServerType.BUKKIT) {
                    return self = new Bukkit();
                }
            } else {
                return self = new Dummy();
            }
        }
        return self;
    }

    public abstract boolean isVanished(Object player);

    public boolean isVisible(Object player) {
        return !isVanished(player);
    }

    public static boolean isPresent() {
        try {
            Class.forName("de.myzelyam.api.vanish.VanishAPI");
            return true;
        } catch (ClassNotFoundException e) {}
        return false;
    }

    // implementations

    private static class Dummy extends VanishAPI {
        @Override public boolean isVanished(Object player) {
            return false;
        }
    }

    private static class Bungee extends VanishAPI {
        @Override public boolean isVanished(Object player) {
            return de.myzelyam.api.vanish.BungeeVanishAPI.isInvisible((ProxiedPlayer)player);
        }
    }

    private static class Bukkit extends VanishAPI {
        @Override
        public boolean isVanished(Object player) {
            return de.myzelyam.api.vanish.VanishAPI.isInvisible((Player)player);
        }
    }
}
