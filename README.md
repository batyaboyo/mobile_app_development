# The Word - Android Bible App

Native Android Bible study app built with Kotlin and Jetpack Compose.

## Features

- Bible reading from HelloAO API
- Translation, book, and chapter selection
- Local verse bookmarks using SharedPreferences
- Stories and quiz content from bundled JSON assets
- Bottom navigation: Home, Bible, Bookmarks, Stories, Quiz

## Project Structure

```text
bible_app/
├── app/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── ANDROID_README.md
└── README.md
```

## Open in Android Studio

1. Open Android Studio
2. Choose **Open**
3. Select this repository root folder
4. Wait for Gradle sync
5. Run the `app` configuration on an emulator or device

## Requirements

- Android Studio Iguana or newer
- Android SDK 34
- JDK 17

## API

Base URL: `https://bible.helloao.org`

Used endpoints:

- `/api/available_translations.json`
- `/api/{translation}/books.json`
- `/api/{translation}/{book}/{chapter}.json`

## License

MIT
