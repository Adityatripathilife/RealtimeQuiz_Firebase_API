package com.example.advance_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.advance_application.ui.theme.Advance_ApplicationTheme
import com.example.advance_application.ui.theme.LeaderboardScreen
import com.example.advance_application.ui.theme.QuizScreen
import com.example.advance_application.ui.theme.SplashScreen
import com.example.advance_application.ui.theme.StartGameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Advance_ApplicationTheme {
                Surface {
                    // Screens: "splash", "start", "quiz", "leaderboard"
                    var currentScreen by remember { mutableStateOf("splash") }

                    when (currentScreen) {
                        "splash" -> {
                            SplashScreen(
                                onTimeout = { currentScreen = "start" }
                            )
                        }
                        "start" -> {
                            StartGameScreen(
                                onStartClicked = { currentScreen = "quiz" }
                            )
                        }
                        "quiz" -> {
                            QuizScreen(
                                onViewLeaderboard = { currentScreen = "leaderboard" }
                            )
                        }
                        "leaderboard" -> {
                            LeaderboardScreen(
                                onBackClick = { currentScreen = "quiz" }
                            )
                        }
                    }
                }
            }
        }
    }
}