<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Codfy Changelog

## Unreleased

## 0.2.4 - 2023-12-07

### Changed

- Compatible with build 233.*

## 0.2.3 - 2023-10-06

### Fixed

- Refresh token service
- When changing device or volume, it no longer generates a quick pause in the music

## 0.2.2 - 2023-10-04

### Fixed

- Refresh token

## 0.2.1 - 2023-09-29

### Fixed

- Fixed notify in services when song it was updated
- Fixed Player when codfy panel was hidden

## 0.2.0 - 2023-09-28

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

## 0.1.4 - 2023-09-26

### Changed

- Improvement on reconnect spotify

## 0.1.3 - 2023-09-19

### Fixed

- Fixed layout render in first access
- Added missing permission scope

## 0.1.2 - 2023-09-18

### Changed

- Removed Authorization Scope not used

### Fixed

- Fixed login when it was first access
- Fixed status bar height

## 0.1.1 - 2023-09-17

### Added

- BadGatewayException is ignored when error

### Changed

- Moved files

### Fixed

- Fixed label of player
- Fixed Playlist render

## 0.1.0 - 2023-09-15

### Added

- Release Beta version
