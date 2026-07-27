package com.example.advance_application.model

// Individual Question Item
data class Question(
    val id: String = "",
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0
)

// Player Profile & Score Sync
data class Player(
    val id: String = "",
    val name: String = "",
    val score: Int = 0,
    val isReady: Boolean = false,
    val selectedOptionIndex: Int = -1
)

// Game Room Current State
enum class GameStatus {
    WAITING,
    STARTED,
    QUESTION_ACTIVE,
    LEADERBOARD,
    FINISHED
}

// Complete Live Quiz Room Structure for Firebase Sync
data class QuizRoom(
    val roomId: String = "",
    val currentQuestionIndex: Int = 0,
    val status: GameStatus = GameStatus.WAITING,
    val players: Map<String, Player> = emptyMap(),
    val questions: List<Question> = emptyList()
)