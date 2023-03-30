![Spicord Logo](https://i.imgur.com/FniEBrc.png)

[![Latest release](https://img.shields.io/github/v/release/OopsieWoopsie/Spicord?include_prereleases&logo=github&logoColor=white&style=flat-square)](https://github.com/OopsieWoopsie/Spicord/releases/latest)
[![Discord](https://img.shields.io/badge/Support-Discord-blue?logo=discord&logoColor=white&style=flat-square)](https://discord.gg/fBzAwWW)
[![Downloads](https://img.shields.io/github/downloads/OopsieWoopsie/Spicord/total?label=love%20count&logo=git&style=flat-square)](https://github.com/OopsieWoopsie/Spicord/releases)
[![License](https://img.shields.io/github/license/OopsieWoopsie/Spicord?logo=gnu&style=flat-square)](https://github.com/OopsieWoopsie/Spicord/blob/master/LICENSE)

## Features
> This is the list of the current features that Spicord has.

* Multi-bot support
* Custom commands support
  * Different command prefix for each bot!
* Addon support
  * JavaScript support!
  * Integrated simple addons (for testing the plugin)!
  * Independent addons for each bot!
* Integrated easy-to-use API
* Embed messages support
* Optimized code
* Compatible with Spigot/Bukkit
* Compatible with BungeeCord
* Compatible with Velocity
* Compatible with Sponge
* Spicord is open-source!

## Commands
| Command usage | Description |
| --- | --- |
| sp bot \<botname> <add/remove> \<addon-key> | Add or remove an addon for that bot |
| sp stop [botname] | Stop/shutdown a bot |
| sp start [botname] | Start a bot |
| sp restart | Restart the configuration and the bots |
| sp status | Show the status of your bots (ready/offline) |

## Maven repository
```xml
<!-- for Spicord -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Spicord.Spicord</groupId>
    <artifactId>spicord-common</artifactId>
    <version>v5-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

```xml
<!-- JDA -->
<dependency>
    <groupId>net.dv8tion</groupId>
    <artifactId>JDA</artifactId>
    <version>5.0.0-beta.4</version>
    <scope>provided</scope>
</dependency>
```

## Building
Install maven and git (Ubuntu)

`sudo apt install maven git -y`


Clone this repository and 'cd' into it

`git clone https://github.com/OopsieWoopsie/Spicord.git && cd Spicord`


Build the project

`mvn clean verify`


The JAR file for Minecraft server will be located at `minecraft/target/Spicord_VERSION.jar`



**Note**: Spicord uses [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA)
