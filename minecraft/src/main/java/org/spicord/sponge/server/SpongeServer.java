package org.spicord.sponge.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.EventListenerRegistration;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.plugin.PluginContainer;

import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.player.UniversalPlayer;
import eu.mcdb.util.SLF4JWrapper;
import net.kyori.adventure.text.Component;

public class SpongeServer extends eu.mcdb.universal.Server {

    private final Logger logger = new SLF4JWrapper();

    private final Game game;
    private final PluginContainer plugin;
    private final Server server;

    public SpongeServer(Game game, PluginContainer plugin) {
        this.game = game;
        this.plugin = plugin;
        this.server = game.server();
    }

    @Override
    public int getOnlineCount() {
        return server.onlinePlayers().size();
    }

    @Override
    public int getPlayerLimit() {
        return server.maxPlayers();
    }

    @Override
    public String[] getOnlinePlayers() {
        return server.onlinePlayers().stream()
                .map(Player::name)
                .toArray(String[]::new);
    }

    @Override
    public Map<String, List<String>> getServersAndPlayers() {
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("default", Arrays.asList(getOnlinePlayers()));

        return map;
    }

    @Override
    public String getVersion() {
        return "Sponge " + game.platform().minecraftVersion().name();
    }

    @Override
    public String[] getPlugins() {
        return game.pluginManager().plugins().stream()
                .map(c -> c.metadata().name().orElse(c.metadata().id()))
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        try {
            return server.commandManager().process(game.systemSubject(), command).isSuccess();
        } catch (CommandException e) {
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
        final Optional<ServerPlayer> player = server.player(uuid);

        if (player.isPresent()) {
            return new SpongePlayer(player.get());
        }

        return null;
    }

    @Override
    public void broadcast(String message) {
        server.broadcastAudience().sendMessage(Component.text(message));
    }

    public <T extends Event> Runnable registerListener(Class<T> event, Consumer<T> handler) {
        EventListener<T> listener = e -> handler.accept(e);

        EventListenerRegistration<T> listenerRegistration = EventListenerRegistration.builder(event)
            .plugin(plugin)
            .listener(listener)
            .build();

        EventManager eventManager = game.eventManager();

        eventManager.registerListener(listenerRegistration);

        return () -> eventManager.unregisterListeners(listener);
    }

    @Override
    public void registerCommand(Object plugin, UniversalCommand command) {
        SpongeCommandExecutor.register(plugin, command);
    }
}
