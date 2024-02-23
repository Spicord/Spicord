package org.spicord.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.player.UniversalPlayer;

public class DummyServer extends Server {

    @Override
    public int getOnlineCount() {
        return 0;
    }

    @Override
    public int getPlayerLimit() {
        return 0;
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
        getLogger().info(String.format("Tried to dispatch command: '%s'", command));
        return true;
    }

    @Override
    public Logger getLogger() {
        return Logger.getAnonymousLogger();
    }

    @Override
    public UniversalPlayer getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public void broadcast(String message) {
        getLogger().info("Broadcast: " + message);
    }

    @Override
    public void registerCommand(Object plugin, UniversalCommand command) {
    }
}
