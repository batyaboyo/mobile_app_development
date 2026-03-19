# Android Setup Notes

This repository is now Android-only and already arranged as a standard Android Studio project at the repository root.

## Included Modules

- `app`: main Android application module

## Key Source Paths

- `app/src/main/java/com/batyaboyo/bibleapp/MainActivity.kt`
- `app/src/main/java/com/batyaboyo/bibleapp/ui/TheWordApp.kt`
- `app/src/main/java/com/batyaboyo/bibleapp/data/ApiService.kt`
- `app/src/main/java/com/batyaboyo/bibleapp/data/LocalStore.kt`
- `app/src/main/java/com/batyaboyo/bibleapp/data/AssetRepository.kt`
- `app/src/main/assets/stories.json`
- `app/src/main/assets/quiz.json`

## Run

1. Open repository root in Android Studio
2. Let Gradle sync
3. Run `app` on emulator/device

## Next Improvements

1. Add commentary endpoint integration
2. Add richer search UI and navigation
3. Replace placeholder launcher icons
