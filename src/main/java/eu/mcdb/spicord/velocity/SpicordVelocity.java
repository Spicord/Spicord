<<<<<<< HEAD
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

=======
>>>>>>> 41854b324e3d05c7046534531eda7df28818b7b3
package eu.mcdb.spicord.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.universal.MCDB;
import eu.mcdb.universal.plugin.VelocityPlugin;

@Plugin(id = "spicord", name = "Spicord", version = "2.6.0", authors = { "OopsieWoopsie" })
public class SpicordVelocity extends VelocityPlugin {

    @Inject
    public SpicordVelocity(ProxyServer proxyServer) {
        super(proxyServer);
    }

    @Override
    public void onEnable() {
        SpicordLoader loader = new SpicordLoader(getLogger(), getDataFolder());
        loader.load();
        MCDB.registerCommand(this, new SpicordCommand(() -> {}));
    }
}
