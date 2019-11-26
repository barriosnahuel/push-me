# Welcome!

*Here are some useful notes when making changes on this project.*

## Local setup

1. Clone/Fork this repo.
2. Replace the `app/google-services.json` file with the one from Firebase console. You *won't* be able to commit changes on this file.
3. Run:
    > ./gradlew check

    It must return **`BUILD SUCCESS`**.

## Continuous Integration
We use Circle CI, so if you're gonna change the [config.yml](.circleci/config.yml) file you can check the config using the local CLI.
- https://circleci.com/docs/2.0/local-cli

> circleci config validate

## Firebase config file

To prevent future modifications on `app/google-services.json` I run:

    >  git update-index --skip-worktree app/google-services.json

    To revert this just:

    > git update-index --no-skip-worktree app/google-services.json

## Firebase Performance Monitoring

You can filter logcat messages by:

> adb logcat -s FirebasePerformance

## Resources
- Icon generated using: [romannurik.github.io/AndroidAssetStudio/icons-launcher](http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html)
- In-App icons using: [Material Design resources](https://material.io/resources/icons/?style=baseline)

## Signing

The following files must be located into the root dir:
- `nahuelbarrios.keystore`
- `secure.properties`