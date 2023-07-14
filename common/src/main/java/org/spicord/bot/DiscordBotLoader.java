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

package org.spicord.bot;

import java.util.logging.Logger;

import org.spicord.Spicord;
import org.spicord.bot.DiscordBot.BotStatus;

import com.google.common.base.Preconditions;

public class DiscordBotLoader {

    // FIXME: Do not use Spicord.getInstance()
    private static final Logger logger = Spicord.getInstance().getLogger();

    /**
     * Start a bot.
     * 
     * @param bot the bot to be started
     * @return true if the bot successfully started
     */
    public static boolean startBot(DiscordBot bot) {
        Preconditions.checkNotNull(bot, "bot");

        if (bot.isEnabled()) {
            if (bot.getStatus() == BotStatus.OFFLINE) {
                logger.info("Starting bot '" + bot.getName() + "'.");

                Spicord.getInstance().getThreadPool().execute(() -> bot.start());

                return true;
            } else {
                logger.warning("Can't start bot '" + bot.getName() + "', status: " + bot.getStatus());
            }
        } else {
            logger.warning("Bot '" + bot.getName() + "' is disabled. Skipping.");
        }

        return false;
    }

    /**
     * Shutdown the given bot.
     * 
     * @param bot the bot instance
     */
    public static void shutdownBot(DiscordBot bot) {
        Preconditions.checkNotNull(bot, "bot");
        bot.shutdown();
    }
}
