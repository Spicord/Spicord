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

package org.spicord.addon;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.spicord.api.addon.JavaScriptAddon;
import org.spicord.api.addon.JavaScriptBaseAddon;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.script.ScriptEngine;
import org.spicord.script.ScriptEnvironment;
import org.spicord.script.ScriptException;
import com.google.gson.Gson;

import eu.mcdb.util.FileUtils;
import eu.mcdb.util.ZipExtractor;

/**
 * This class is used for manage and register addons.
 */
public class AddonManager {

    private static final Set<SimpleAddon> addons;

    static {
        addons = Collections.synchronizedSet(new HashSet<SimpleAddon>());
    }

    private final Logger logger;

    public AddonManager(Logger logger) {
        this.logger = logger;
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
     * Check if the given addon id is registered.
     * 
     * @param id the addon id
     * @return true if the addon is registered
     */
    public boolean isRegistered(String id) {
        return addons.stream()
                .map(SimpleAddon::getId)
                .anyMatch(id::equals);
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
            final Object[] args = new Object[] { addon.getName(), addon.getId(), addon.getAuthor() };

            logger.info(String.format("Registered addon '%s' (%s) by %s", args));

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
     * Unregister an addon by its id.
     * 
     * @param id the addon id
     * @return true if it was unregistered, or false if not
     */
    public boolean unregisterAddon(String id) {
        return addons.removeIf(addon -> addon.getId().equals(id));
    }

    /**
     * Get an addon instance by its id.
     * 
     * @param id the addon id
     * @return the addon instance, or null if not found
     */
    public SimpleAddon getAddonById(String id) {
        checkNotNull(id, "The addon id cannot be null.");
        checkArgument(!id.trim().isEmpty(), "The addon id cannot be empty.");

        for (final SimpleAddon addon : addons)
            if (addon.getId().equals(id))
                return addon;

        logger.warning("The addon with the id '" + id + "' was not found.");
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

        bot.getAddons().stream()
                .map(this::getAddonById)
                .filter(Objects::nonNull)
                .forEach(bot::loadAddon);
    }

    public Set<SimpleAddon> getAddons() {
        return addons;
    }

    /**
     * 
     * @param dir
     */
    public void loadAddons(File addonsDir) {
        checkNotNull(addonsDir);

        final File runtimeDir = new File(addonsDir, ".runtime-" + System.nanoTime());
        runtimeDir.mkdirs();
        FileUtils.deleteOnExit(runtimeDir);

        for (final String name : addonsDir.list()) {
            if (name.startsWith(".")) continue;

            final File file = new File(addonsDir, name);

            if (file.isDirectory()) {
                this.loadDirAddon(file);
            } else if (name.endsWith(".sp") || name.endsWith(".zip")) {
                this.loadZipAddon(file, runtimeDir);
            }
        }
    }

    private static final Gson GSON = new Gson();

    private void loadZipAddon(final File file, final File runtimeDir) {
        try (final ZipExtractor ex = new ZipExtractor(file)) {
            final Optional<Reader> entry = ex.readEntry("addon.json");

            if (entry.isPresent()) {
                final Reader reader = entry.get();
                final AddonData data = GSON.fromJson(reader, AddonData.class);

                final String id      = checkNotNull(data.getId(), "id");
                final String name    = data.getName()    == null ? id : data.getName();
                final String author  = data.getAuthor()  == null ? "unknown" : data.getAuthor();
                final String version = data.getVersion() == null ? "unknown" : data.getVersion();
                final String main    = checkNotNull(data.getMain(), "main");
                final String engineName  = "rhino";

                if (!ex.hasEntry(main)) {
                    throw new ScriptException(main + " not found for addon " + name);
                }

                final File tempDir = new File(runtimeDir, id);
                tempDir.mkdirs();

                ex.extract(tempDir);

                File dataDir = new File(tempDir, "data");

                if (dataDir.exists()) {
                    final File addonsDir = FileUtils.getParent(runtimeDir);
                    final File addonDir = new File(addonsDir, name);

                    if (!addonDir.exists()) {
                        dataDir.renameTo(addonDir);
                    }
                    dataDir = addonDir;
                }

                final File addonMain = new File(tempDir, main);

                final ScriptEnvironment env = new ScriptEnvironment()
                        .addEnv("__data", dataDir.toString());

                final ScriptEngine engine = ScriptEngine.getEngine(engineName);
                final Object res = engine.loadScript(addonMain, env);

                if (res instanceof JavaScriptBaseAddon) {
                    final JavaScriptAddon addon = new JavaScriptAddon(name, id, author, version, (JavaScriptBaseAddon) res, engine);
                    this.registerAddon(addon);
                } else {
                    throw new ScriptException("the '" + main + "' file needs to export the addon instance");
                }
            } else {
                logger.warning(String.format(
                        "The file '%s' doesn't contains the 'addon.json' file on its root directory, ignoring it",
                        file.getName()));
            }
        } catch (IOException e) {
            logger.warning(String.format("The file '%s' cannot be loaded: %s", file.getName(), e.getMessage()));
        }
    }

    private void loadDirAddon(final File addonDir) {
        try {
            final File addonJson = new File(addonDir, "addon.json");

            if (addonJson.exists()) {
                final Reader reader = new FileReader(addonJson);
                final AddonData data = GSON.fromJson(reader, AddonData.class);

                final String id      = checkNotNull(data.getId(), "id");
                final String name    = data.getName()    == null ? id : data.getName();
                final String author  = data.getAuthor()  == null ? "unknown" : data.getAuthor();
                final String version = data.getVersion() == null ? "unknown" : data.getVersion();
                final String main    = checkNotNull(data.getMain(), "main");
                final String engineName = "rhino";

                final File addonMain = new File(addonDir, main);

                if (!addonMain.exists()) {
                    throw new ScriptException(main + " not found in addon " + name);
                }

                final File dataDir = new File(addonDir, "data");

                final ScriptEnvironment env = new ScriptEnvironment()
                        .addEnv("__data", dataDir.toString());

                final ScriptEngine engine = ScriptEngine.getEngine(engineName);
                final Object res = engine.loadScript(addonMain, env);

                if (res instanceof JavaScriptBaseAddon) {
                    final JavaScriptAddon addon = new JavaScriptAddon(name, id, author, version, (JavaScriptBaseAddon) res, engine);
                    this.registerAddon(addon);
                } else {
                    throw new ScriptException("the '" + main + "' file needs to export the addon instance");
                }
            }
        } catch (IOException e) {
            logger.warning(String.format("The addon on folder '%s' cannot be loaded: %s", addonDir.getName(), e.getMessage()));
        }    
    }
}
