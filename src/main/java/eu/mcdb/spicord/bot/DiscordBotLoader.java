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

package eu.mcdb.spicord.bot;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.bot.DiscordBot.BotStatus;

public class DiscordBotLoader {

    private static final Logger logger = Spicord.getInstance().getLogger();

    /**
     * Loads the given bot.
     * 
     * @param bot the bot instance.
     * @return true if the bot successfully started.
     */
    public static boolean startBot(DiscordBot bot) {
        Preconditions.checkNotNull(bot, "bot");

        if (bot.isEnabled()) {
            if (bot.getStatus() == BotStatus.OFFLINE) {
                bot.status = BotStatus.STARTING;
                logger.info("Starting bot '" + bot.getName() + "'.");
                CompletableFuture.runAsync(() -> bot.startBot());
                return true;
            } else {
                logger.warning("Bot '" + bot.getName() + "' has already started.");
            }
        } else {
            logger.warning("Bot '" + bot.getName() + "' is disabled. Skipping.");
        }

        return false;
    }

    /**
     * Shutdowns the given bot if it is enabled.
     * 
     * @param bot the bot instance.
     * @return true if the bot was stopped.
     */
    public static boolean shutdownBot(DiscordBot bot) {
        Preconditions.checkNotNull(bot, "bot");

        if (bot.isReady()) {
            bot.status = BotStatus.STOPPING;
            bot.jda.shutdownNow();
            bot.jda = null;
            bot.status = BotStatus.OFFLINE;
            return true;
        }
        return false;
    }

    /**
     * @param bot the bot to be disabled
     * @deprecated As of snapshot 2.0.0, use {@link #shutdownBot(DiscordBot)}
     *             instead.
     */
    @Deprecated
    public static void disableBot(DiscordBot bot) {
        shutdownBot(bot);
    }
}
