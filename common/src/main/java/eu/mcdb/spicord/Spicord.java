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

package eu.mcdb.spicord;

import java.util.function.Consumer;
import java.util.logging.Logger;
import org.spicord.api.services.ServiceManager;
import eu.mcdb.spicord.addon.AddonManager;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.universal.ServerType;
import lombok.Getter;

/**
 * @deprecated use org.spicord.Spicord
 */
@Deprecated
public class Spicord {

    private static Spicord instance;

    @Getter private Logger logger;
    @Getter private ServerType serverType;
    @Getter private SpicordConfiguration config;
    @Getter private ServiceManager serviceManager;
    @Getter private AddonManager addonManager;

    protected Spicord() {
        instance = this;
    }

    public void onLoad(Consumer<Spicord> action) {}

    public DiscordBot getBotByName(String name) {return null;}

    public void debug(String message) {}

    public static Spicord getInstance() {
        return instance;
    }

    public static String getVersion() {
        return Spicord.class.getPackage().getImplementationVersion();
    }

    public static boolean isLoaded() {
        return instance != null;
    }
}
