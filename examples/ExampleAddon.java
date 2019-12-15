package examples;

import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;

public class ExampleAddon extends SimpleAddon {

    public ExampleAddon() {
        super("Pong", "pong", "Sheidy", new String[] { "ping", "hello" });
    }

    @Override
    public void onCommand(DiscordBotCommand command, String[] args) {
        switch (command.getName()) {
        case "ping":
            command.reply("Pong!");
            break;
        case "hello":
            command.reply("Hello " + command.getAuthorAsMention() + "!");
            break;
        }
    }
}
