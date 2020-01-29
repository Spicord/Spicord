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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.spicord.script.ScriptEngine;
import org.spicord.script.ScriptException;
import com.google.gson.Gson;
import eu.mcdb.spicord.api.Node;
import eu.mcdb.spicord.api.addon.JavaScriptAddon;
import eu.mcdb.spicord.api.addon.JavaScriptBaseAddon;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.util.ZipExtractor;
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
        checkNotNull(key, "The addon key cannot be null.");
        checkArgument(!key.trim().isEmpty(), "The addon key cannot be empty.");

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
        checkNotNull(bot);

        if (bot.getAddons().isEmpty())
            return;

        bot.getAddons().stream().map(this::getAddonByKey).filter(Objects::nonNull).forEach(bot::loadAddon);
    }

    public void loadAddons(File dir) {
        checkNotNull(dir);

        final File addonsDir = new File(dir, "addons");
        final File runtimeDir = new File(addonsDir, ".runtime-" + System.nanoTime());
        runtimeDir.mkdirs();
        runtimeDir.deleteOnExit();

        for (final String name : addonsDir.list()) {
            if (name.endsWith(".sp") || name.endsWith(".zip")) {
                final File file = new File(addonsDir, name);
                if (file.isFile()) this.loadZipAddon(file, runtimeDir);
            }
        }
    }

    private static final Gson GSON = new Gson();
    private static final ScriptEngine ENGINE = ScriptEngine.getEngine("nashorn");

    private void loadZipAddon(final File file, final File runtimeDir) {
        try (final ZipExtractor ex = new ZipExtractor(file)) {
            final Optional<Reader> entry = ex.readEntry("addon.json");

            if (entry.isPresent()) {
                final Reader reader = entry.get();
                final AddonData data = GSON.fromJson(reader, AddonData.class);

                final String key = checkNotNull(data.getKey(), "key");
                final String name = checkNotNull(data.getName(), "name");
                final String author = checkNotNull(data.getAuthor(), "author");
                final String main;

                if (ex.hasEntry("index.js")) {
                    main = "index.js";
                } else if (ex.hasEntry("src/index.js")) {
                    main = "src/index.js";
                } else {
                    throw new ScriptException("index.js not found for addon " + name);
                }

                final File tempDir = new File(runtimeDir, key);
                tempDir.mkdirs();

                ex.extract(tempDir);

                final File addonMain = new File(tempDir, main);
                final Object res = ENGINE.java(ENGINE.require(addonMain));

                if (res instanceof JavaScriptBaseAddon) {
                    final JavaScriptAddon addon = new JavaScriptAddon(name, key, author, (JavaScriptBaseAddon) res, ENGINE);
                    this.registerAddon(addon);
                } else {
                    throw new ScriptException("the index.js file needs to export the addon instance");
                }
            } else {
                getLogger().warning(String.format(
                        "The file '%s' doesn't contains the 'addon.json' file on its root directory, ignoring it",
                        file.getName()));
            }
        } catch (IOException e) {
            getLogger().warning(String.format("The file '%s' cannot be loaded: %s", file.getName(), e.getCause()));
        }
    }
}
