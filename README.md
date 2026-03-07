# Quick Math (Android)

Quick Math is a native Android app built with **Kotlin** and **Jetpack Compose**. It helps keep your mind sharp with short math quiz sessions.

## Clean build in Android Studio

1. **Open the project:** File → Open → select this folder (the one containing `build.gradle.kts` and `app/`).
2. **Sync:** Let Android Studio sync the Gradle project. If prompted, use the default Gradle wrapper; the IDE will download Gradle and any missing wrapper files as needed.
3. **Clean build:** Build → Clean Project, then Build → Rebuild Project.
4. **Run or build APK:** Run → Run 'app', or Build → Build Bundle(s) / APK(s) → Build APK(s).

Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Requirements

- Android Studio Ladybug (2024.2.1) or newer (or IDE with Android Gradle Plugin 8.2+)
- JDK 17
- Android SDK: compileSdk 35, minSdk 26

## Project structure

```
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/quickmath/app/    # Application, MainActivity, data, UI
│       └── res/                        # strings, colors, themes, drawable
├── gradle/wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew
```

## Features

- Onboarding: language, OTP (simplified), privacy, overlay permission
- Home: start/stop session, quiz cards (full/half screen), fun pop-ups with vibration
- Settings: display mode, overlay permission, vibration, fun pop-ups
- Subscribe and About screens
