package com.example.advance_application.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advance_application.data.LeaderboardItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun LeaderboardScreen(onBackClick: () -> Unit) {
    val scoreList = remember { mutableStateListOf<LeaderboardItem>() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Leaderboard")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scoreList.clear()
                for (childSnapshot in snapshot.children) {
                    val id = childSnapshot.key ?: ""
                    val playerName = childSnapshot.child("playerName").getValue(String::class.java) ?: "Unknown"
                    val score = childSnapshot.child("score").getValue(Int::class.java) ?: 0
                    val timestamp = childSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L

                    scoreList.add(LeaderboardItem(id, playerName, score, timestamp))
                }
                scoreList.sortByDescending { it.score } // Highest score top par
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onBackClick, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                    Text("Back")
                }
                Text(
                    text = "🏆 Global Leaderboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.width(50.dp)) // balancing space
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 40.dp))
            } else if (scoreList.isEmpty()) {
                Text(
                    text = "No scores on leaderboard yet!",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 40.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(scoreList) { index, item ->
                        val rank = index + 1
                        val crownOrMedal = when (rank) {
                            1 -> "👑 "
                            2 -> "🥈 "
                            3 -> "🥉 "
                            else -> "#$rank "
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (rank == 1) Color(0xFFFFD700).copy(alpha = 0.2f) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = crownOrMedal,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.playerName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1C1B1F)
                                    )
                                }
                                Text(
                                    text = "${item.score} PTS",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6200EE)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}