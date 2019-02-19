# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- The *DiscordBotLoader#disableBot(DiscordBot)* method.
- More error messages.

### Changed
- Some Javadoc comments.
- Organized the imports.

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
