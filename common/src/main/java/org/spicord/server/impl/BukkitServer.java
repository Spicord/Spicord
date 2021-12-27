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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.spicord.player.BukkitPlayer;
import org.spicord.util.VanishAPI;
import eu.mcdb.universal.player.UniversalPlayer;

final class BukkitServer extends eu.mcdb.universal.Server {

    private final Server bukkit = Bukkit.getServer();

    @Override
    public int getOnlineCount() {
        return bukkit.getOnlinePlayers().size();
    }

    @Override
    public int getPlayerLimit() {
        return bukkit.getMaxPlayers();
    }

    @Override
    public String[] getOnlinePlayers() {
        final VanishAPI vanish = VanishAPI.get();
        return bukkit.getOnlinePlayers().stream()
                .filter(vanish::isVisible)
                .map(Player::getName)
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
        return bukkit.getVersion();
    }

    @Override
    public String[] getPlugins() {
        return Stream.of(bukkit.getPluginManager().getPlugins())
                .map(Plugin::getName)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        Boolean result = callSyncMethod(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        return result == null ? false : result;
    }

    private <T> T callSyncMethod(Callable<T> task) {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("Spicord");
            return Bukkit.getScheduler().callSyncMethod(plugin, task).get(1000, TimeUnit.SECONDS);
        } catch (Exception e) {}
        return null;
    }

    @Override
    public Logger getLogger() {
        return bukkit.getLogger();
    }

    @Override
    public UniversalPlayer getPlayer(UUID uuid) {
        final OfflinePlayer player = bukkit.getOfflinePlayer(uuid);

        if (!player.isOnline()) {
            return null;
        }

        return new BukkitPlayer(player.getPlayer());
    }

    @Override
    public void broadcast(String message) {
        bukkit.broadcastMessage(message);
    }
}
