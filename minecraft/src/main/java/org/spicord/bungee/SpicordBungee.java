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

package org.spicord.bungee;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.spicord.fix.FixClassLoaderPosition;
import eu.mcdb.spicord.LibraryLoader;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.universal.MCDB;
import net.md_5.bungee.api.plugin.Plugin;

public class SpicordBungee extends Plugin {

    private SpicordLoader loader;

    @Override
    public void onEnable() {
        if (new File(getDataFolder(), "forceload.txt").exists()) {
            getLogger().info("Libraries will be forced to load");
            LibraryLoader.setForceLoad(true);
        }

        Runnable reload = () -> {
            onDisable();
            this.loader = new SpicordLoader(getLogger(), getDataFolder());
        };
        reload.run();

        getProxy().getScheduler().schedule(this, () -> loader.load(), 10, TimeUnit.SECONDS);

        MCDB.registerCommand(this, new SpicordCommand(() -> {
            reload.run();
            loader.load();
        }));

        if (new File(getDataFolder(), "fixloader.txt").exists()) {
            try {
                if (FixClassLoaderPosition.bungee()) {
                    getLogger().info("Successfully applied the Loader fix");
                }
            } catch (Exception e) {
                getLogger().warning("An error ocurred while applying the Loader fix: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        if (loader != null)
            loader.disable();

        this.loader = null;
    }
}
