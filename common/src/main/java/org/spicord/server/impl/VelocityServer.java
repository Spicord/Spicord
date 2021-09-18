/*
 * Copyright (C) 2020  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord.server.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.spicord.player.VelocityPlayer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.ProxyVersion;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.player.UniversalPlayer;
import eu.mcdb.util.SLF4JWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

final class VelocityServer extends Server {

    private static ProxyServer handle;

    private final Logger logger = new SLF4JWrapper();

    @Override
    public int getOnlineCount() {
        return handle.getPlayerCount();
    }

    @Override
    public int getPlayerLimit() {
        // From docs: "This allows you to customize the number of “maximum” players in
        // the player’s server list. Note that Velocity doesn’t have a maximum number of
        // players it supports."
        return handle.getConfiguration().getShowMaxPlayers();
    }

    @Override
    public String[] getOnlinePlayers() {
        return handle.getAllPlayers().stream()
                .map(Player::getUsername)
                .toArray(String[]::new);
    }

    @Override
    public Map<String, List<String>> getServersAndPlayers() {
        final Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (RegisteredServer server : handle.getAllServers()) {
            List<String> players = server.getPlayersConnected().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());

            map.put(server.getServerInfo().getName(), players);
        }

        return map;
    }

    @Override
    public String getVersion() {
        final ProxyVersion version = handle.getVersion();
        return version.getName() + " " + version.getVersion();
    }

    @Override
    public String[] getPlugins() {
        return handle.getPluginManager().getPlugins().stream()
                .map(plugin -> plugin.getDescription().getName())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        CompletableFuture<Boolean> future = handle.getCommandManager().executeAsync(handle.getConsoleCommandSource(), command);
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
        final Optional<Player> player = handle.getPlayer(uuid);

        if (player.isPresent()) {
            return new VelocityPlayer(player.get());
        }

        return null;
    }

    protected static void setHandle(ProxyServer server) {
        if (server == null) return;
        if (handle != null) return;

        handle = server;
    }

    @Override
    public void broadcast(String message) {
        TextComponent text = Component.text(message);
        for (Player player : handle.getAllPlayers()) {
            player.sendMessage(text);
        }
    }
}
