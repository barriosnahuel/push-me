# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][], and this project adheres to [Semantic Versioning][].

## \[unreleased]
### Added
- Let users share packaged buttons too.
- Let users delete custom audios by swiping horizontally.
- Target Android 12L (API Level 32); Tests also run for that API level.
- Pull Requests template for contributors.
- Firebase Analytics.
- Firebase Crashlytics including last logs from each error.
- Firebase Performance Monitoring.
- Added Detekt static code analysis tool, as well as Ktlint for code style conventions.
- Setup [Stale GitHub app](https://github.com/apps/stale).
- Codacy checks integration through GitHub.
- Multidex support for debug builds.
- Timestamp as suffix on `versionName` for debug builds.
- Automatic code obfuscation and optimizations on release builds.

### Changed
- Brand NEW Look & Feel for the whole app.
- Min Android version required is KitKat (API Level 19).
- Circle CI config migrated to v2.1 using Orbs.
- Many dependencies to the latest versions including Gradle, Android Build Tools, AppCompat,
Firebase and static code analyzers too.
- Migration to Android X.
- Checkstyle turned on again.
- Findbugs replaced with Spotbugs starting with `static-code-analysis` v3.x.
- New ':model' module in Kotlin.
- New ':commons_file' and ':commons_android' modules.
- Changelog formatting.
- Default language to English.
- Gradle version to [v7.3.3](https://docs.gradle.org/7.2/release-notes.html).
- Java compile version to v11.
- Better separation of responsibilities.
- `StrictMode` warnings now are logged to Crashlytics instead of the debugging notifier.
- `LeakCanary` warnings now are logged to Crashlytics.
- Refactor of Media Player setup for clearness.
- Replaced Stetho with [Flipper](https://fbflipper.com/).

### Fixed
- Sharing buttons issues due to a bad permissions setup.
- Crashes when debugging because of a bug in library `StrictModeNotifier`.
- Performance issue when saving a new audio button.
- Memory leak at the `MediaPlayer` which causes an increment on memory usage.
- Many static code checks suggestions applied.
- Usage of resources like `InputStream`, `OutputStream` and `Cursor`.
- Stop exposing Firebase API configuration file.

### Removed
- Library StrictModeNotifier for debug builds.
- Library [Static Code Analysis](https://github.com/Monits/static-code-analysis-plugin) because it's no longer actively maintained.

## \[v1.1.0] - 2017-08-16

### Added
- **Share saved audios after long press.** ![bitmoji](https://render.bitstrips.com/v2/cpanel/8363918-196115675_6-s4-v1.png?transparent=1&palette=1&width=246)
- Support for Android O (API level 26).
- Static code checks to improve code quality using SCA.
- Setup release signing certificate.
- Test tools for the debug version: Stetho, different applicatonId and icon, Leak Canary.

### Fixed
- Performance when scrolling.

### Changed
- Migrated to Gradle wrapper v4.1 as well as Android build tools.
- Refactor.

## \[v1.0.1] - 2017-08-16

### Fixed
- Support for Content URI format when creating new buttons.

## \[v1.0.0] - 2017-08-16
### Added
- Predefined audio messages.
- Save audio messages directly from WhatsApp by just sharing them.

[keep a changelog]: https://keepachangelog.com/en/1.0.0/
[semantic versioning]: https://semver.org/spec/v2.0.0.html
