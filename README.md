# 🏃 Fake Step Writer

A simple Android app to write fake step count data directly to **Health Connect**. Useful for testing apps that read health data from Health Connect.

## ✨ Features

- **Write Steps** — Enter any step count and write it to Health Connect instantly
- **Realistic Distribution** — Steps are split across 13 natural walking sessions throughout the day (6:30 AM to 9:30 PM) so the data looks authentic
- **Read Steps** — Verify today's total step count from Health Connect
- **Smart Time Handling** — Only creates sessions for past hours, never writes future timestamps

## 📱 Screenshots

The app features a clean dark UI with:
- Custom app logo
- Step count input field
- Write & Read buttons
- Real-time status feedback

## 📥 Download

Download the latest APK from the [releases](https://github.com/ayshishannidhya/fake-step-writer/releases) or grab it directly from the `app/build/outputs/apk/debug/` folder.

## 🛠️ Requirements

- Android 14+ (API 34)
- Health Connect app installed on device
- Health Connect permissions granted (the app will prompt you)

## 🚀 How to Use

1. Install the APK on your Android device
2. Open **Fake Step Writer**
3. Grant Health Connect permissions when prompted
4. Enter the desired step count (default: 20,000)
5. Tap **"Write Steps to Health Connect"**
6. Verify with **"Read Today's Steps"** or check your health apps

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

## ⚠️ Disclaimer

This app is intended for **testing and development purposes only**. Writing fake health data may violate the terms of service of some health and fitness apps. Use responsibly.

## 👨‍💻 Author

**Made by Ayshi Shannidhya Panda (Ankit)**

## 📜 License

This project is open source and available for personal use.
