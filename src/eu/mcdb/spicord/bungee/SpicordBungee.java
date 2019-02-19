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
import eu.mcdb.spicord.SpicordLoader.ServerType;
import net.md_5.bungee.api.plugin.Plugin;

public class SpicordBungee extends Plugin {

	private Spicord spicord;
	private SpicordLoader loader;
	private static SpicordBungee instance;

	@Override
	public void onEnable() {
		instance = this;
		this.spicord = new Spicord(getLogger());
		this.loader = new SpicordLoader(spicord, getClass().getClassLoader());
		loader.setServerType(ServerType.BUNGEECORD);
		loader.setDisableAction((OopsieWoopsie) -> {
			this.spicord = null;
			this.loader = null;
			instance = null;
		});
		getProxy().getScheduler().schedule(this, () -> loader.load(), 3, TimeUnit.SECONDS);
	}

	@Override
	public void onDisable() {
		if (loader != null)
			loader.disable();
	}

	public Spicord getSpicord() {
		return spicord;
	}

	public static SpicordBungee getInstance() {
		return instance;
	}
}