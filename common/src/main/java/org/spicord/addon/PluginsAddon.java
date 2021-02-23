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

package org.spicord.addon;

import java.awt.Color;
import org.spicord.Spicord;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import eu.mcdb.universal.Server;
import net.dv8tion.jda.core.EmbedBuilder;

public class PluginsAddon extends SimpleAddon {

    public PluginsAddon() {
        super("Plugin List", "spicord::plugins", "Sheidy", new String[] { "plugins" });
    }

    @Override
    public void onCommand(DiscordBotCommand command, String[] args) {
        final Server server = Server.getInstance();

        final EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Plugins (" + server.getPlugins().length + "): ")
                .setDescription(String.join(", ", server.getPlugins()))
                .setColor(new Color(5154580));

        String footer = getSpicord().getConfig().getIntegratedAddonFooter();

        if (footer == null || footer.isEmpty()) {
            // ignore
        } else {
            builder.setFooter(footer.replace("{version}", Spicord.getVersion()), null);
        }

        command.reply(builder.build());
    }
}
