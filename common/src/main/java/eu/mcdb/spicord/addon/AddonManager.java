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

package eu.mcdb.spicord.addon;

import java.io.File;
import java.util.Set;
import eu.mcdb.spicord.api.Node;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;

/**
 * This class is used for manage and register addons.
 */
@Deprecated
public class AddonManager implements Node {

    /**
     * Check if the given addon is registered.
     * 
     * @param addon the addon
     * @return true if the addon is registered
     */
    public boolean isRegistered(SimpleAddon addon) {return false;}

    /**
     * Check if the given addon id is registered.
     * 
     * @param id the addon id
     * @return true if the addon is registered
     */
    public boolean isRegistered(String id) {return false;}

    /**
     * Register an addon.
     * 
     * @param addon the addon
     * @return true if the addon was successfully registered, or false if it was
     *         already registered
     */
    public boolean registerAddon(SimpleAddon addon) {return false;}

    /**
     * Unregister an addon.
     * 
     * @param addon the addon instance
     * @return true if it was unregistered, or false if not
     */
    public boolean unregisterAddon(SimpleAddon addon) {return false;}

    /**
     * Unregister an addon by its id.
     * 
     * @param id the addon id
     * @return true if it was unregistered, or false if not
     */
    public boolean unregisterAddon(String id) {return false;}

    /**
     * Get an addon instance by its id.
     * 
     * @param id the addon id
     * @return the addon instance, or null if not found
     */
    public SimpleAddon getAddonById(String id) {return null;}

    /**
     * Load the available addons for the given bot.
     * 
     * @param bot the bot that will load its addons
     */
    public void loadAddons(DiscordBot bot) {}

    public Set<SimpleAddon> getAddons() {return null;}

    public void loadAddons(File addonsDir) {}
}
