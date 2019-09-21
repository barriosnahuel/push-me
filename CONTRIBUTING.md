# Welcome!

*Here are some useful notes when making changes on this project.*

## Local setup

1. Download the `google-services.json` file from Firebase console to the `app/` directory.
2. Run:
    > ./gradlew check

    It must return **`BUILD SUCCESS`**.

## Continuous Integration
We use Circle CI, so if you're gonna change the [config.yml](.circleci/config.yml) file you can check the config using the local CLI.
- https://circleci.com/docs/2.0/local-cli

> circleci config validate

## Firebase Performance Monitoring

You can filter logcat messages by:

> adb logcat -s FirebasePerformance

## Resources
- Icon generated using: [romannurik.github.io/AndroidAssetStudio/icons-launcher](http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html)
