# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][], and this project adheres to [Semantic Versioning][].

## \[unreleased]
### Added
- Support for Android P (API Level 28).
- Pull Requests template for contributors.
- Firebase Analytics. 
- Firebase Crashlytics.
- Firebase Performance Monitoring.
- `ajcore*.txt` file deleted when running `./gradlew clean`.
- Added Detekt static code analysis tool, as well as Ktlint for code style conventions.
- Setup [Stale GitHub app](https://github.com/apps/stale).
- Codacy checks integration through GitHub.

### Changed
-Circle CI config migrated to v2.1 using Orbs.
- Many dependencies to the latest versions including Gradle, Android Build Tools and static code analyzers too.
- Migration to Android X.
- Checkstyle turned on again.
- New ':model' module in Kotlin.
- New ':commons_file' and ':commons_android' modules.
- Changelog formatting.

### Fixed
- Many SCA suggestions applied.

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
