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

package org.spicord.bukkit;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import org.spicord.SpicordCommand;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;

public class SpicordBukkit extends JavaPlugin implements SpicordPlugin {

    private SpicordLoader loader;

    @Override
    public void onEnable() {
        checkForceload();

        Runnable reload = () -> {
            onDisable();
            this.loader = new SpicordLoader(getLogger(), getDataFolder());
        };
        reload.run();

        int delay = loader.getConfig().getLoadDelay();
        getLogger().info("Spicord will load in " + delay + " seconds");
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> loader.load(), delay * 20);

        new SpicordCommand(() -> {
            reload.run();
            loader.load();
        }).register(this);

        checkLoader(true);
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void onDisable() {
        if (loader != null)
            loader.shutdown();

        this.loader = null;
    }
}
