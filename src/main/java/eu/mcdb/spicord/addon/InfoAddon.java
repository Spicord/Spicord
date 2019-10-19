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

package eu.mcdb.spicord.addon;

import java.awt.Color;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import net.dv8tion.jda.core.EmbedBuilder;

public class InfoAddon extends SimpleAddon {

    public InfoAddon() {
        super("Server Information", "spicord::info", "OopsieWoopsie");
    }

    @Override
    public void onLoad(DiscordBot bot) {
        bot.onCommand("info", this::statusCommand);
    }

    private void statusCommand(DiscordBotCommand command) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Server information")
                .setDescription("Online players: " + getServer().getOnlineCount() + "/" + getServer().getPlayerLimit()
                        + "\nServer version: " + getServer().getVersion())
                .setColor(new Color(5154580)).setFooter("Powered by Spicord", null);

        command.getMessage().getChannel().sendMessage(builder.build()).queue();
    }
}
