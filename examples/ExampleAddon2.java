package examples;

import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;

public class ExampleAddon2 extends SimpleAddon {

    public ExampleAddon2() {
        super("Playing Status", "playing-status", "Sheidy");
    }

    @Override
    public void onReady(DiscordBot bot) {
        JDA jda = bot.getJda();

        // this will make your bot show "Playing with Spicord" on its status
        jda.getPresence().setGame(Game.playing("with Spicord"));

        // be able to change the status with the -playing command
        // use: -playing <status>
        // if you will use this make sure to only allow admins to use the command
        bot.onCommand("playing", c -> {
            String[] args = c.getArguments();

            if (args.length > 0) {
                String newStatus = String.join(" ", args);
                jda.getPresence().setGame(Game.playing(newStatus));
            } else {
                c.reply(c.getAuthorAsMention() + ", please use **" + c.getPrefix() + c.getName() + " <status>**");
            }
        });
    }
}
