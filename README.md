# 🚀 RealtimeQuizAPI

A modern Android Quiz Application built with **Kotlin**, **Jetpack Compose**, **Firebase Realtime Database**, and the **Open Trivia DB API**. The application fetches live quiz questions from the internet, calculates scores in real time, and stores player scores on a global leaderboard using Firebase.

-

# 🌟 About the Project

RealtimeQuizAPI is a modern Android quiz application that demonstrates **API integration**, **Firebase Realtime Database**, and **Jetpack Compose** in a real-world project.

Unlike traditional quiz applications that use hardcoded questions, this app fetches fresh quiz questions from the **Open Trivia DB API** every time a new game starts. Player scores are stored in **Firebase Realtime Database**, allowing users to compete on a shared leaderboard with live updates.

The application is built using **Kotlin** and follows a clean, state-driven architecture with **Jetpack Compose** and **Kotlin Coroutines** to provide a smooth and responsive user experience.

---

# ✨ Features

- 🌐 Fetches live quiz questions from the Open Trivia DB API
- ☁️ Stores player scores in Firebase Realtime Database
- 🏆 Live global leaderboard with automatic score updates
- 🎯 Randomized multiple-choice answers for every question
- ⏳ 15-second countdown timer for each quiz
- 🎨 Modern UI built entirely with Jetpack Compose
- ⚡ Asynchronous networking using Kotlin Coroutines
- 🧹 Cleans HTML entities returned by the API
- 📱 Responsive Material 3 user interface
- 🔄 State-driven UI with automatic recomposition

---

# 📱 Application Flow

```text
Splash Screen
      │
      ▼
Start Game Screen
      │
      ▼
Quiz Screen
      │
      ▼
Result Screen
      │
      ▼
Global Leaderboard
```

### 🚀 Splash Screen
Displays the application logo and smoothly navigates to the home screen.

### 🎮 Start Game Screen
Provides a clean interface where users can begin a new quiz.

### ❓ Quiz Screen
- Fetches quiz questions from the Open Trivia DB API
- Randomizes answer options
- Displays a live 15-second countdown timer
- Calculates the player's score in real time

### 📝 Result Screen
- Displays the final score
- Allows users to enter their name
- Saves the score to Firebase Realtime Database

### 🏆 Leaderboard
- Retrieves scores from Firebase
- Automatically updates when new scores are added
- Displays rankings from highest to lowest

---

# 🏗️ Architecture

```text
Open Trivia DB API
          │
          ▼
HttpURLConnection
          │
          ▼
JSON Parsing (org.json)
          │
          ▼
Jetpack Compose UI
          │
          ▼
Firebase Realtime Database
          │
          ▼
Global Leaderboard
```

---

# 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Programming Language |
| Jetpack Compose | Modern Android UI Toolkit |
| Material 3 | UI Components |
| Firebase Realtime Database | Cloud Database |
| Kotlin Coroutines | Background Processing |
| HttpURLConnection | API Requests |
| org.json | JSON Parsing |
| Android Studio | Development IDE |

---

# 📂 Project Structure

```text
app
│
├── ui
│   ├── SplashScreen
│   ├── StartGameScreen
│   ├── QuizScreen
│   ├── ResultScreen
│   └── LeaderboardScreen
│
├── model
├── network
├── firebase
│
└── MainActivity.kt
```

---

# ⚙️ Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/RealtimeQuizAPI.git
```

### 2️⃣ Open in Android Studio

Open the project using **Android Studio Hedgehog, Iguana, Koala, or a newer version**.

### 3️⃣ Configure Firebase

- Create a Firebase Project
- Enable **Realtime Database**
- Download the `google-services.json` file
- Place it inside the **app/** directory

### 4️⃣ Add Internet Permission

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### 5️⃣ Build & Run

- Sync Gradle
- Connect an Android device or emulator
- Run the application

---

# 🌐 API Used

### Open Trivia Database

https://opentdb.com/

The application fetches fresh multiple-choice quiz questions directly from the Open Trivia Database using HTTP requests and parses the JSON response using `org.json`.

---

# 🚀 Future Improvements

- 🔐 Firebase Authentication
- 🌙 Dark Mode
- 🎵 Sound Effects
- 📊 Player Statistics
- 🏅 Achievement System
- 📚 Category Selection
- 🎯 Difficulty Levels
- 🌍 Multiplayer Quiz Support

---

# 👨‍💻 Author

**Aditya Tripathi**

- 💻 GitHub: https://github.com/YOUR_USERNAME
- 🔗 LinkedIn: https://linkedin.com/in/YOUR_PROFILE

---

## ⭐ Support

If you found this project helpful or interesting, consider giving it a **⭐ Star** on GitHub. It helps others discover the project and motivates future improvements.

---
