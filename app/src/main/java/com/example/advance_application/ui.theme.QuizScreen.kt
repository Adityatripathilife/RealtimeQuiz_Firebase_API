package com.example.advance_application.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advance_application.model.Question
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun QuizScreen(onViewLeaderboard: () -> Unit) {
    val questionsList = remember { mutableStateListOf<Question>() }
    var isLoadingQuestions = remember { mutableStateOf(true) }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var isCorrectAnswer by remember { mutableStateOf<Boolean?>(null) }
    var timeLeft by remember { mutableIntStateOf(15) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }

    var playerName by remember { mutableStateOf("") }
    var isScoreSaved by remember { mutableStateOf(false) }

    // API se Live Questions Fetch Karna (Open Trivia DB)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL("https://opentdb.com/api.php?amount=5&type=multiple")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseString = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(responseString)
                val resultsArray = jsonObject.getJSONArray("results")

                val fetchedQuestions = mutableListOf<Question>()
                for (i in 0 until resultsArray.length()) {
                    val item = resultsArray.getJSONObject(i)
                    val qText = item.getString("question")
                        .replace("&quot;", "\"")
                        .replace("&#039;", "'")
                        .replace("&amp;", "&")

                    val correctAns = item.getString("correct_answer")
                        .replace("&quot;", "\"")
                        .replace("&#039;", "'")

                    val incorrectArray = item.getJSONArray("incorrect_answers")
                    val options = mutableListOf<String>()
                    for (j in 0 until incorrectArray.length()) {
                        options.add(
                            incorrectArray.getString(j)
                                .replace("&quot;", "\"")
                                .replace("&#039;", "'")
                        )
                    }

                    // Sahi jawab ko random option position par dalna
                    val correctIndex = (0..3).random()
                    options.add(correctIndex, correctAns)

                    fetchedQuestions.add(
                        Question(
                            id = (i + 1).toString(),
                            questionText = qText,
                            options = options,
                            correctOptionIndex = correctIndex
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    if (fetchedQuestions.isNotEmpty()) {
                        questionsList.clear()
                        questionsList.addAll(fetchedQuestions)
                    }
                    isLoadingQuestions.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    isLoadingQuestions.value = false
                }
            }
        }
    }

    if (isLoadingQuestions.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF6200EE))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Fetching new questions.", color = Color.Gray)
            }
        }
        return
    }

    if (questionsList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Check Internet connection !.", color = Color.Red)
        }
        return
    }

    val currentQuestion = questionsList[currentQuestionIndex]

    // Timer Logic
    LaunchedEffect(currentQuestionIndex, timeLeft, isQuizFinished) {
        if (!isQuizFinished && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (!isQuizFinished && timeLeft == 0 && selectedIndex == -1) {
            selectedIndex = -2
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6200EE).copy(alpha = 0.12f),
                        Color(0xFFF8F9FA),
                        Color(0xFFFFFFFF)
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isQuizFinished) {
                // --- RESULT SCREEN ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🎉 Quiz Completed!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Your Total Score", fontSize = 16.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$score / ${questionsList.size * 100} PTS",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = playerName,
                                onValueChange = { playerName = it },
                                label = { Text("Enter Your Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (playerName.isNotBlank() && !isScoreSaved) {
                                        val database = FirebaseDatabase.getInstance()
                                        val scoreRef = database.getReference("Leaderboard").push()
                                        val data = mapOf(
                                            "playerName" to playerName,
                                            "score" to score,
                                            "timestamp" to System.currentTimeMillis()
                                        )
                                        scoreRef.setValue(data)
                                        isScoreSaved = true
                                    }
                                },
                                enabled = !isScoreSaved && playerName.isNotBlank(),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
                            ) {
                                Text(text = if (isScoreSaved) "Score Saved! ✅" else "Save Score to Leaderboard", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onViewLeaderboard,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "View Leaderboard 🏆", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                // --- QUIZ PLAYING SCREEN ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "QUESTION ${currentQuestionIndex + 1} OF ${questionsList.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Score: $score PTS",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6200EE)
                        )
                    }

                    AnimatedCircularTimer(
                        totalTimeSeconds = 15,
                        currentTimeSeconds = timeLeft,
                        modifier = Modifier.size(80.dp)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        text = currentQuestion.questionText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF1C1B1F),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    currentQuestion.options.forEachIndexed { index, optionText ->
                        ShakeAndScaleOptionCard(
                            text = optionText,
                            isSelected = selectedIndex == index,
                            isCorrect = if (selectedIndex == index) isCorrectAnswer else null,
                            onClick = {
                                if (selectedIndex == -1) {
                                    selectedIndex = index
                                    val correct = (index == currentQuestion.correctOptionIndex)
                                    isCorrectAnswer = correct
                                    if (correct) score += 100
                                }
                            }
                        )
                    }
                }

                if (selectedIndex != -1) {
                    Button(
                        onClick = {
                            if (currentQuestionIndex < questionsList.size - 1) {
                                currentQuestionIndex++
                                timeLeft = 15
                                selectedIndex = -1
                                isCorrectAnswer = null
                            } else {
                                isQuizFinished = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentQuestionIndex < questionsList.size - 1) "Next Question" else "View Results",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}