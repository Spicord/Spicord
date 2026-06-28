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
