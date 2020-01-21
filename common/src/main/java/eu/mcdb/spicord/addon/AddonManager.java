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

package eu.mcdb.spicord.addon;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.api.Node;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import lombok.Getter;

/**
 * This class is used for manage and register addons.
 */
public class AddonManager implements Node {

    @Getter
    private static final Set<SimpleAddon> addons;

    static {
        addons = Collections.synchronizedSet(new HashSet<SimpleAddon>());
    }

    /**
     * Check if the given addon is registered.
     * 
     * @param addon the addon
     * @return true if the addon is registered
     */
    public boolean isRegistered(SimpleAddon addon) {
        return addons.contains(addon);
    }

    /**
     * Check if the given addon key is registered.
     * 
     * @param key the addon key
     * @return true if the addon is registered
     */
    public boolean isRegistered(String key) {
        return addons.stream().map(SimpleAddon::getKey).anyMatch(key::equals);
    }

    /**
     * Register an addon.
     * 
     * @param addon the addon
     * @return true if the addon was successfully registered, or false if it was
     *         already registered
     */
    public boolean registerAddon(SimpleAddon addon) {
        if (!isRegistered(addon)) {
            final Object[] args = new Object[] { addon.getName(), addon.getKey(), addon.getAuthor() };

            getLogger().info(String.format("Registered addon '%s' (%s) by %s", args));

            return addons.add(addon);
        }
        return false;
    }

    /**
     * Unregister an addon.
     * 
     * @param addon the addon instance
     * @return true if it was unregistered, or false if not
     */
    public boolean unregisterAddon(SimpleAddon addon) {
        return addons.remove(addon);
    }

    /**
     * Unregister an addon by its key.
     * 
     * @param key the addon key
     * @return true if it was unregistered, or false if not
     */
    public boolean unregisterAddon(String key) {
        return addons.removeIf(addon -> addon.getKey().equals(key));
    }

    /**
     * Get an addon instance by its key.
     * 
     * @param key the addon key
     * @return the addon instance, or null if not found
     */
    public SimpleAddon getAddonByKey(String key) {
        Preconditions.checkNotNull(key, "The addon key cannot be null.");
        Preconditions.checkArgument(!key.trim().isEmpty(), "The addon key cannot be empty.");

        for (final SimpleAddon addon : addons)
            if (addon.getKey().equals(key))
                return addon;

        getLogger().warning("The addon with the key '" + key + "' was not found.");
        return null;
    }

    /**
     * Load the available addons for the given bot.
     * 
     * @param bot the bot that will load its addons
     */
    public void loadAddons(DiscordBot bot) {
        Preconditions.checkNotNull(bot);

        if (bot.getAddons().isEmpty())
            return;

        bot.getAddons().stream().map(this::getAddonByKey).filter(Objects::nonNull).forEach(bot::loadAddon);
    }
}
