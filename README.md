![Spicord Logo](https://i.imgur.com/FniEBrc.png)

[![Latest release](https://img.shields.io/github/release/OopsieWoopsie/Spicord.svg)](https://github.com/OopsieWoopsie/Spicord/releases/latest)
[![License](https://img.shields.io/github/license/OopsieWoopsie/Spicord.svg)](https://github.com/OopsieWoopsie/Spicord/blob/master/LICENSE)

## Features
> This is the list of the current features that Spicord has.
* Multi-bot support
* Custom commands support
  * Different command prefix for each bot!
* Addon support
  * Integrated simple addons (for testing the plugin)!
  * Independent addons for each bot!
* Integrated easy-to-use API
* Spicord is open-source!

## Implementation
### Creating an Addon
```java
// Example Addon
package test;

import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;

public class ExampleAddon extends SimpleAddon {

    public ExampleAddon() {
        super(
            "Example Addon", // Addon name
            "my_example_addon", // Addon key
            "OopsieWoopsie" // Addon author
        );
    }

    @Override
    public void onLoad(DiscordBot bot) {
        // You don't need to put a command like "!example" or "-example", because the command prefix is configured apart.
        bot.onCommand("example", example -> {
            example.getMessage().getChannel()
                .sendMessage("Hello world!").queue();
        });
    }
}
```
### Registering the Addon
```java
// Example using the BungeeCord plugin API, you can use the Bukkit API if you want ^^
package test;

import eu.mcdb.spicord.Spicord;
import net.md_5.bungee.api.plugin.Plugin;

public class ExamplePlugin extends Plugin {

    @Override
    public void onEnable() {
        if (Spicord.isLoaded()) { // Check if Spicord was loaded
            Spicord.getInstance().getAddonManager().registerAddon(new ExampleAddon()); // Register the addon
        } else {
            // You need to add Spicord as a dependency on your plugin, so this will never happen :D
            System.out.println("Spicord was not loaded :c");
        }
    }
}
```
### Add Spicord as a dependency
> Probably your `plugin.yml` or `bungee.yml` file looks like this:
```yaml
name: ExamplePlugin
author: OopsieWoopsie
version: 1.0
main: test.ExamplePlugin
```
> It needs to have the `depend` section, like this:
```yaml
name: ExamplePlugin
author: OopsieWoopsie
version: 1.0
main: test.ExamplePlugin
depend: [Spicord] # Add this line
```
### Using the Addon on your bot
Your `config.yml` looks like this:
```yaml
bots:
  default:
    command-support: true
    command-prefix: "-"
    token: "YOUR BOT TOKEN HERE"
    enabled: false
    addons:
    - "spicord::info"
    - "spicord::plugins"
    - "spicord::players"
```
* You need to change `YOUR BOT TOKEN HERE` with your bot token, if you don't know how to do that, you should look at [this tutorial](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token).
* Then, change the `enabled` option value to `true`.
* By default, your `addons` section looks like this:
```yaml
    addons:
    - "spicord::info"
    - "spicord::plugins"
    - "spicord::players"
```
> To enable your addon on that bot you need to add the addon key to that list. Example:
```yaml
    addons:
    - "spicord::info"
    - "spicord::plugins"
    - "spicord::players"
    - "my_example_addon" # this is the addon key used on the example
```
> The values starting with `spicord::` are the integrated Spicord addons, and can be disabled by removing them from the `addons` section.
* When you start your server, the bot will start and the addons will be loaded.
* So, our bot prefix is `-`, if we send the message `-example` to a channel who the bot can read, this will happen:
![Working bot](https://i.imgur.com/a8H8O5E.png)
* Enjoy doing cool stuff ^^

## Integrated addons
Spicord has 3 integrated addons, they are:
* `Server Information` (command: `info`)
* `Player List` (command: `players`)
* `Plugin List` (command: `plugins`)

> And that's how the commands look in operation:

![Info command](https://i.imgur.com/IwbntNw.png)

![Players command](https://i.imgur.com/iRCvBo9.png)

![Plugins command](https://i.imgur.com/IAIAk2z.png)


**Note**: Spicord uses [Java Discord API, also known as JDA](https://github.com/DV8FromTheWorld/JDA), you should take a look at that!
