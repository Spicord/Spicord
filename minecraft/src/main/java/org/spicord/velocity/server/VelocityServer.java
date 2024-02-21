package org.spicord.velocity.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.spicord.player.VelocityPlayer;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.ProxyVersion;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.player.UniversalPlayer;
import eu.mcdb.util.SLF4JWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class VelocityServer extends Server {

    private final Logger logger = new SLF4JWrapper();

    private final ProxyServer server;
    private final Object plugin;

    public VelocityServer(ProxyServer server, Object plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public int getOnlineCount() {
        return server.getPlayerCount();
    }

    @Override
    public int getPlayerLimit() {
        // From docs: "This allows you to customize the number of “maximum” players in
        // the player’s server list. Note that Velocity doesn’t have a maximum number of
        // players it supports."
        return server.getConfiguration().getShowMaxPlayers();
    }

    @Override
    public String[] getOnlinePlayers() {
        return server.getAllPlayers().stream()
                .map(Player::getUsername)
                .toArray(String[]::new);
    }

    @Override
    public Map<String, List<String>> getServersAndPlayers() {
        final Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (RegisteredServer server : server.getAllServers()) {
            List<String> players = server.getPlayersConnected().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());

            map.put(server.getServerInfo().getName(), players);
        }

        return map;
    }

    @Override
    public String getVersion() {
        final ProxyVersion version = server.getVersion();
        return version.getName() + " " + version.getVersion();
    }

    @Override
    public String[] getPlugins() {
        return server.getPluginManager().getPlugins().stream()
                .map(plugin -> plugin.getDescription().getName())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        CompletableFuture<Boolean> future = server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public UniversalPlayer getPlayer(UUID uuid) {
        final Optional<Player> player = server.getPlayer(uuid);

        if (player.isPresent()) {
            return new VelocityPlayer(player.get());
        }

        return null;
    }

    @Override
    public void broadcast(String message) {
        TextComponent text = Component.text(message);
        for (Player player : server.getAllPlayers()) {
            player.sendMessage(text);
        }
    }

    public <T> Runnable registerListener(Class<T> event, Consumer<T> handler) {
        EventManager eventManager = server.getEventManager();

        EventHandler<T> listener = e -> handler.accept(e);

        eventManager.register(
            plugin,
            event,
            listener
        );

        return () -> eventManager.unregister(plugin, listener);
    }
}
