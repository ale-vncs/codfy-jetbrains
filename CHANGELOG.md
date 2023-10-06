<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Codfy Changelog

## [Unreleased]

## [0.2.3] - 2023-10-06

### Fixed

- Refresh token service
- When changing device or volume, it no longer generates a quick pause in the music

## [0.2.2] - 2023-10-04

### Fixed

- Refresh token

## [0.2.1] - 2023-09-29

### Fixed

- Fixed notify in services when song it was updated
- Fixed Player when codfy panel was hidden

## [0.2.0] - 2023-09-28

### Added

- Adjustment volume of sound
- Adjustment device manager

### Changed

- Refactoring in player control
- Refactoring in SpotifyPlayerTrackUpdate
- Refactoring in SpotifyRefreshTokenService
- Dependencies - upgrade `se.michaelthelin.spotify:spotify-web-api-java` to `8.1.0`
- Improvements in playlist area

### Fixed

- Fixed track progressbar action
- Fixed layout stuck when multiple ide instance open
- Fixed player don't show when track is playing

## [0.1.4] - 2023-09-26

### Changed

- Improvement on reconnect spotify

## [0.1.3] - 2023-09-19

### Fixed

- Fixed layout render in first access
- Added missing permission scope

## [0.1.2] - 2023-09-18

### Changed

- Removed Authorization Scope not used

### Fixed

- Fixed login when it was first access
- Fixed status bar height

## [0.1.1] - 2023-09-17

### Added

- BadGatewayException is ignored when error

### Changed

- Moved files

### Fixed

- Fixed label of player
- Fixed Playlist render

## [0.1.0] - 2023-09-15

### Added

- Release Beta version

[Unreleased]: https://github.com/JetBrains/intellij-platform-plugin-template/compare/v0.2.3...HEAD
[0.2.3]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.2.2...v0.2.3
[0.2.2]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.2.1...v0.2.2
[0.2.1]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.1.4...v0.2.0
[0.1.4]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/ale-vncs/codfy-jetbrains/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/ale-vncs/codfy-jetbrains/commits/v0.1.0
