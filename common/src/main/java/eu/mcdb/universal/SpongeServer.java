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

package eu.mcdb.universal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import eu.mcdb.universal.player.UniversalPlayer;
import eu.mcdb.util.SLF4JWrapper;

class SpongeServer extends eu.mcdb.universal.Server {

    private final Game game;
    private final Server server;
    private final Logger logger;

    SpongeServer() {
        this.game = Sponge.getGame();
        this.server = Sponge.getServer();
        this.logger = new SLF4JWrapper();
    }

    @Override
    public int getOnlineCount() {
        return server.getOnlinePlayers().size();
    }

    @Override
    public int getPlayerLimit() {
        return server.getMaxPlayers();
    }

    @Override
    public String[] getOnlinePlayers() {
        return server.getOnlinePlayers().stream()
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
        return "Sponge " + game.getPlatform().getMinecraftVersion().getName();
    }

    @Override
    public String[] getPlugins() {
        return game.getPluginManager().getPlugins().stream()
                .map(PluginContainer::getName)
                .toArray(String[]::new);
    }

    @Override
    public boolean dispatchCommand(String command) {
        game.getCommandManager().process(server.getConsole(), command);
        return true;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public UniversalPlayer getPlayer(UUID uuid) {
        final Player player = server.getPlayer(uuid).orElse(null);

        if (player == null)
            return null;

        return new UniversalPlayer(player.getName(), uuid) {

            @Override
            public Player getSpongePlayer() {
                return player;
            }
        };
    }
}
