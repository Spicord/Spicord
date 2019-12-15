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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.ProxyVersion;
import eu.mcdb.spicord.velocity.SLF4JWrapper;
import lombok.Setter;

class VelocityServer extends Server {

    @Setter
    private static ProxyServer handle;

    @Override
    public int getOnlineCount() {
        return handle.getPlayerCount();
    }

    @Override
    public int getPlayerLimit() {
        // TODO: ...
        return getOnlineCount();
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
                .map(p -> p.getDescription().getName())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        return handle.getCommandManager().execute(handle.getConsoleCommandSource(), command);
    }

    @Override
    public boolean isVelocity() {
        return true;
    }

    @Override
    public Logger getLogger() {
        return new SLF4JWrapper();
    }
}
