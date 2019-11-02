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
import eu.mcdb.spicord.Spicord;

class DummyServer extends Server {

    @Override
    public int getOnlineCount() {
        return 0;
    }

    @Override
    public int getPlayerLimit() {
        return 100;
    }

    @Override
    public String[] getOnlinePlayers() {
        return new String[0];
    }

    @Override
    public Map<String, List<String>> getServersAndPlayers() {
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("default", Arrays.asList(getOnlinePlayers()));

        return map;
    }

    @Override
    public String getVersion() {
        return "DummyServer 1.0";
    }

    @Override
    public String[] getPlugins() {
        return new String[] { "Spicord" };
    }

    @Override
    public boolean dispatchCommand(String command) {
        getLogger().info(String.format("Tried to dispatch command: %s", command));
        return true;
    }

    @Override
    public Logger getLogger() {
        return Spicord.getInstance().getLogger();
    }
}
