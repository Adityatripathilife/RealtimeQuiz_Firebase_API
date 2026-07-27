package com.example.advance_application.data

data class LeaderboardItem(
    val id: String = "",
    val playerName: String = "Unknown Player",
    val score: Int = 0,
    val timestamp: Long = 0L
)