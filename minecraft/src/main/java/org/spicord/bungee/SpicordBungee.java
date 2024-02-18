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

import java.util.concurrent.TimeUnit;

import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.bungee.server.BungeeServer;
import org.spicord.fix.Fixes;

import eu.mcdb.universal.Server;
import net.md_5.bungee.api.plugin.Plugin;

public class SpicordBungee extends Plugin implements SpicordPlugin {

    private SpicordLoader loader;

    @Override
    public void reloadSpicord() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = new SpicordLoader(this);
        this.loader.load();
    }

    @Override
    public Spicord getSpicord() {
        return this.loader.getSpicord();
    }

    @Override
    public void onLoad() {
        Fixes.checkForceload(this);

        Server.setInstance(new BungeeServer(getProxy()));

        this.loader = new SpicordLoader(this);
    }

    @Override
    public void onEnable() {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> {
            BungeeJDADetector.checkOtherJDA(this);
            loader.load();
        }, loadDelay, TimeUnit.SECONDS);

        Fixes.checkLoader(this, false);
    }

    @Override
    public void onDisable() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = null;
    }
}
