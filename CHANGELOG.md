# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1.0-SNAPSHOT] - 2019-08-14
### Added
- The **-players \<serverName\>** command (only for BungeeCord).
- The license header to some classes.

### Changed
- Now when using BungeeCord, the **-players** message is displayed in
  other format (suggested by Sharkie).

### Deprecated
- The *Conversions#toJdaEmbed(Embed)* method.

### Fixed
- [Cyrillic letters are not displayed](https://github.com/OopsieWoopsie/BanAnnouncer/issues/2)
- The arguments lenght of the commands.

## [2.0.0-SNAPSHOT] - 2019-07-12
### Added
- A new line at the end of the source files. 🙄
- More Javadoc comments.
- The *DiscordBotLoader#shutdownBot(DiscordBot)* method.
- All the embed related things.
- The new library-loading system.
- The */spicord* command for BungeeCord and Bukkit/Spigot.
- Another way to create commands.
- A new logging system for JDA.

### Changed
- Replaced the tabs with 4 spaces.
- Renamed class *SpicordUtils* to *ReflectionUtils*.
- Moved the enum *SpicordLoader.ServerType* to package *eu.mcdb.util*
- Moved the class *Server* to package *eu.mcdb.util*
- Now the libraries only will be extracted if it not exists on the lib folder.

### Deprecated
- The *DiscordBot#setEnabled(boolean)* method, the value is final and cannot be changed.
- The *DiscordBot#setDisabled(boolean)* method, the value is final and cannot be changed.
- The *DiscordBotLoader#disableBot(DiscordBot)* method.
- The *ISpicord#startBot(DiscordBot)* method, use *DiscordBotLoader#startBot(DiscordBot)* instead.
- The *ISpicord#shutdownBot(DiscordBot)* method, use *DiscordBotLoader#shutdownBot(DiscordBot)* instead.
- The *Spicord#startBot(DiscordBot)* method, use *DiscordBotLoader#startbot(DiscordBot)* instead.
- The *Spicord#shutdownBot(DiscordBot)* method, use *DiscordBotLoader#shutdownBot(DiscordBot)* instead.
- The *SpicordBungee#getSpicord()* method, use *Spicord#getInstance()* instead.
- The *SpicordBukkit#getSpicord()* method, use *Spicord#getInstance()* instead.

### Removed
- The *SpicordLoader#setDisableAction(Consumer<Void>)* method.
- The deprecated *Server#getMaxOnlineCount()* method.
- The *CustomMap* class.

## [1.1.0-SNAPSHOT] - 2019-07-01
### Changed
- (Only) In this release, JDA will not print any message in the console.

### Fixed
- Fixed issue #6 (https://github.com/OopsieWoopsie/Spicord/issues/6)
- Fixed issue #8 (https://github.com/OopsieWoopsie/Spicord/issues/8)

## [1.0.4-SNAPSHOT] - 2019-02-28
### Fixed
- Fixed issue #1 (https://github.com/OopsieWoopsie/Spicord/issues/1)

## [1.0.3-SNAPSHOT] - 2019-02-26
### Changed
- Spicord will load 10 seconds after the server starts.

## [1.0.2-SNAPSHOT] - 2019-02-20
### Added
- The *DiscordBotLoader#disableBot(DiscordBot)* method.
- More error messages.

### Changed
- Some Javadoc comments.
- Organized the imports.
- Rewritten the *SpicordLoader#extractLibraries()* method.
- More Exceptions handled.

### Deprecated
- The *Server#getMaxOnlineCount()* method, replaced by *Server#getPlayerLimit()*.

## [1.0.1-SNAPSHOT] - 2019-02-17
### Added
- This CHANGELOG file to hopefully serve as an evolving example of a
  standardized open source project CHANGELOG.
- The *DiscordBot#isReady()* method.
- The *DiscordBot#loadAddon(SimpleAddon)* method.
- More error messages.

### Changed
- More Exceptions handled.

### Removed
- CustomLogger class.
- Unnecessary 'this' references.

### Fixed
- Non thread-safe fields.
