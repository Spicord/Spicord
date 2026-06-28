package eu.mcdb.universal;

import org.spicord.reflect.ReflectUtils;

public enum ServerType {

    BUKKIT     ("org.bukkit.Bukkit"),
    VELOCITY   ("com.velocitypowered.api.proxy.ProxyServer"),
    BUNGEECORD ("net.md_5.bungee.api.ProxyServer"),
    SPONGE     ("org.spongepowered.api.Game"),
    UNKNOWN    (null);

    private final String serverClass;

    private ServerType(String serverClass) {
        this.serverClass = serverClass;
    }

    private boolean isCurrent() {
        if (serverClass == null) {
            return false;
        }

        return ReflectUtils.findClass(serverClass).isPresent();
    }

    public static ServerType auto() {
        if (System.getProperty("SPICORD_CONSOLE", "false").equals("true")) {
            return UNKNOWN;
        }

        for (ServerType serverType : ServerType.values()) {
            if (serverType.isCurrent()) {
                return serverType;
            }
        }

        return UNKNOWN;
    }
}
