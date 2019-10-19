/*
 * Copyright (C) 2019  OopsieWoopsie
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

package eu.mcdb.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class BukkitServer extends eu.mcdb.util.Server {

    private Server bukkit = Bukkit.getServer();

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
        return bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
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
        return Stream.of(bukkit.getPluginManager().getPlugins()).map(Plugin::getName).toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    public boolean isBukkit() {
        return true;
    }

    @Override
    public Logger getLogger() {
        return bukkit.getLogger();
    }
}
