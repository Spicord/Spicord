# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
