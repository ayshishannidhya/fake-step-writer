# 🏃 Fake Step Writer — Add Fake Steps to Health Connect in One Tap

**Free Android app to write any step count directly to Google Health Connect.** Add 10k, 20k, 50k or any custom steps instantly — no root, no walking required. Steps show up in Google Fit, Samsung Health, and every app that reads from Health Connect.

> **TL;DR** — Enter a number, tap a button, your step count is updated. It distributes steps realistically across the day so it looks 100% natural.

---

## ✨ Features

- **One-Tap Step Writing** — Enter any step count (10,000 / 20,000 / 50,000+) and write it to Health Connect instantly
- **Realistic Step Distribution** — Steps are automatically split across 13 natural walking sessions from 6:30 AM to 9:30 PM, so the data looks authentic in Google Fit and other fitness apps
- **Read & Verify Steps** — Check today's total step count from Health Connect right inside the app
- **Smart Time Handling** — Only creates walking sessions for past hours, never writes future timestamps
- **No Root Required** — Works on any stock Android device with Health Connect
- **Completely Free** — No ads, no premium, no sign-up

## 📥 Download APK

[![GitHub Release](https://img.shields.io/github/v/release/ayshishannidhya/fake-step-writer?style=for-the-badge&logo=android&color=3DDC84)](https://github.com/ayshishannidhya/fake-step-writer/releases/latest)

👉 **[Download Latest APK](https://github.com/ayshishannidhya/fake-step-writer/releases/latest/download/Fake-Step-Writer-v1.0.0.apk)** (Android only)

Or browse all versions on the [Releases page](https://github.com/ayshishannidhya/fake-step-writer/releases).

## 📱 Screenshots

The app features a clean dark-themed UI with:
- Custom app logo
- Step count input field
- One-tap Write & Read buttons
- Real-time status feedback

## 🛠️ Requirements

- Android 14 or higher (API 34+)
- [Health Connect](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata) app installed on your device
- Health Connect permissions granted (the app will prompt you automatically)

## 🚀 How to Use

1. Download and install the APK on your Android device
2. Open **Fake Step Writer**
3. Grant Health Connect permissions when prompted
4. Enter the desired step count (default: 20,000)
5. Tap **"Write Steps to Health Connect"**
6. Verify with **"Read Today's Steps"** or check Google Fit / Samsung Health

## 💡 Use Cases

- Testing fitness apps that read step data from Health Connect
- Completing step challenges without actually walking
- Debugging Health Connect API integrations
- Demonstrating step counter functionality

## 🏗️ Building from Source

### Prerequisites
- JDK 17
- Android SDK (API 35)

### Build
```bash
./gradlew assembleDebug
```

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 📦 Tech Stack

- **Language**: Kotlin
- **UI**: Android XML Views
- **Health API**: [AndroidX Health Connect Client](https://developer.android.com/health-and-fitness/guides/health-connect) v1.1.0-alpha07
- **Min SDK**: 34 (Android 14)
- **Target SDK**: 35 (Android 15)

## 📄 Permissions

The app requests the following Health Connect permissions:
- `android.permission.health.READ_STEPS`
- `android.permission.health.WRITE_STEPS`

## 🔍 Keywords

`fake steps health connect` · `add steps to google fit` · `fake step counter android` · `health connect step writer` · `write steps to health connect` · `step hack android` · `google fit step hack` · `samsung health fake steps` · `step counter cheat android` · `health connect api steps`

## ⚠️ Disclaimer

This app is intended for **testing and development purposes only**. Writing fake health data may violate the terms of service of some health and fitness apps. Use responsibly.

## 👨‍💻 Author

**Made by Ayshi Shannidhya Panda (Ankit)**

## 📜 License

This project is open source and available for personal use.
