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

package eu.mcdb.spicord.bot;

import eu.mcdb.spicord.bot.DiscordBot.BotStatus;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.JDA.Status;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class BotStatusListener extends ListenerAdapter {

    private final DiscordBot bot;

    @Override
    public void onReady(ReadyEvent event) {
        bot.status = BotStatus.READY;
        bot.onReady(event);
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        if (event.getNewStatus() == Status.SHUTDOWN) {
            bot.status = BotStatus.OFFLINE;
        }
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        bot.status = BotStatus.READY;
    }

    @Override
    public void onResume(ResumedEvent event) {
        bot.status = BotStatus.READY;
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        bot.status = BotStatus.OFFLINE;
    }
}
