package org.spicord.bungee.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.spicord.player.BungeePlayer;
import org.spicord.util.VanishAPI;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.player.UniversalPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;

public class BungeeServer extends Server {

    private final ProxyServer server;
    private final Plugin plugin;

    private BungeeEventProcessor eventProcessor;

    public BungeeServer(ProxyServer server, Plugin plugin) {
        this.server = server;
        this.plugin = plugin;

        this.eventProcessor = new BungeeEventProcessor();

        server.getPluginManager().registerListener(
            plugin,
            new BungeeListenerAdapter(eventProcessor)
        );
    }

    @Override
    public int getOnlineCount() {
        return server.getOnlineCount();
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getPlayerLimit() {
        return server.getConfig().getPlayerLimit();
    }

    @Override
    public String[] getOnlinePlayers() {
        final VanishAPI vanish = VanishAPI.get();
        return server.getPlayers().stream()
                .filter(vanish::isVisible)
                .map(ProxiedPlayer::getName)
                .toArray(String[]::new);
    }

    @Override
    public Map<String, List<String>> getServersAndPlayers() {
        final Map<String, List<String>> map = new HashMap<String, List<String>>();

        final VanishAPI vanish = VanishAPI.get();
        final Collection<ProxiedPlayer> players = server.getPlayers().stream()
                .filter(vanish::isVisible)
                .collect(Collectors.toList());

        for (final ProxiedPlayer player : players) {
            final String server = getServerName(player);

            if (!map.containsKey(server))
                map.put(server, new ArrayList<String>());

            map.get(server).add(player.getName());
        }
        return map;
    }

    private String getServerName(ProxiedPlayer player) {
        return getServerName(player, "unknown");
    }

    private String getServerName(ProxiedPlayer player, String def) {
        try {
            return player.getServer().getInfo().getName().toString();
        } catch (NullPointerException e) {
            if (isDebugEnabled())
                getLogger().warning("[DEBUG] Cannot get the server name for player '" + (player == null ? "null" : player.getName()) + "', using '" + def + "'.");
        }
        return def;
    }

    @Override
    public String getVersion() {
        String version = server.getVersion();

        if (version.contains(":")) {
            String[] parts = version.split(":");
            if (parts.length == 5) {
                version = String.format("%s %s (%s)", server.getName(), parts[2], parts[3]);
            }
        }

        return version;
    }

    @Override
    public String[] getPlugins() {
        return server.getPluginManager().getPlugins().stream()
                .map(Plugin::getDescription)
                .map(PluginDescription::getName)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        return server.getPluginManager().dispatchCommand(server.getConsole(), command);
    }

    @Override
    public Logger getLogger() {
        return server.getLogger();
    }

    @Override
    public UniversalPlayer getPlayer(UUID uuid) {
        final ProxiedPlayer player = server.getPlayer(uuid);

        if (player == null) {
            return null;
        }

        return new BungeePlayer(player);
    }

    @Override
    public void broadcast(String message) {
        server.broadcast(new TextComponent(message));
    }

    public <T extends Event> Runnable registerListener(Class<T> event, Consumer<T> handler) {
        return eventProcessor.registerEvent(event, handler);
    }
}
