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

package org.spicord.velocity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.plugin.VelocityPlugin;
import org.spicord.reflect.ReflectUtils;
import org.spicord.velocity.server.VelocityServer;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import eu.mcdb.universal.Server;

@Plugin(id = "spicord", name = "Spicord", version = "5.3", authors = { "Tini" })
public class SpicordVelocity extends VelocityPlugin implements SpicordPlugin {

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

    @Inject
    public SpicordVelocity(ProxyServer server) {
        super(server);

        Server.setInstance(new VelocityServer(server, this));

        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = new SpicordLoader(this);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> loader.load(), loadDelay, TimeUnit.SECONDS);
    }

    @Override
    public File getFile() {
        return ReflectUtils.getJarFile(SpicordVelocity.class);
    }
}
