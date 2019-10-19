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

package eu.mcdb.spicord.bungee;

import java.util.concurrent.TimeUnit;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.universal.MCDB;
import eu.mcdb.util.ServerType;
import net.md_5.bungee.api.plugin.Plugin;

public class SpicordBungee extends Plugin {

    private static SpicordBungee instance;
    private SpicordLoader loader;

    @Override
    public void onEnable() {
        instance = this;
        this.loader = new SpicordLoader(getLogger(), getClass().getClassLoader(), ServerType.BUNGEECORD);

        getProxy().getScheduler().schedule(this, () -> loader.load(), 10, TimeUnit.SECONDS);
        MCDB.registerCommand(this, new SpicordCommand());
    }

    @Override
    public void onDisable() {
        if (loader != null)
            loader.disable();

        this.loader = null;
        instance = null;
    }

    /**
     * Gets the Spicord instance
     * 
     * @deprecated As of snapshot 2.0.0, use {@link Spicord#getInstance()} instead.
     * @return the Spicord instance
     */
    @Deprecated
    public Spicord getSpicord() {
        return Spicord.getInstance();
    }

    public static SpicordBungee getInstance() {
        return instance;
    }
}
