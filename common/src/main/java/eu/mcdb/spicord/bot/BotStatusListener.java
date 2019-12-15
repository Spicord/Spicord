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
