package com.example.advance_application.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// 1. Advance Canvas Circular Timer with Dynamic Color Shift
@Composable
fun AnimatedCircularTimer(
    totalTimeSeconds: Int,
    currentTimeSeconds: Int,
    modifier: Modifier = Modifier.size(120.dp)
) {
    val progress = if (totalTimeSeconds > 0) currentTimeSeconds.toFloat() / totalTimeSeconds.toFloat() else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "TimerProgress"
    )

    // Green -> Yellow -> Red color logic based on remaining time
    val strokeColor = when {
        progress > 0.5f -> Color(0xFF4CAF50) // Green
        progress > 0.2f -> Color(0xFFFF9800) // Yellow/Orange
        else -> Color(0xFFF44336)            // Red
    }

    val animatedColor by animateColorAsState(
        targetValue = strokeColor,
        animationSpec = tween(durationMillis = 300),
        label = "TimerColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()

            // Background track
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // Animated Progress Line
            drawArc(
                color = animatedColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$currentTimeSeconds",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = animatedColor
        )
    }
}

// 2. Option Card with Physics Spring Scale & Shake Effects
@Composable
fun ShakeAndScaleOptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }

    // Shake keyframes animation on Wrong Answer
    LaunchedEffect(isCorrect) {
        if (isCorrect == false) {
            coroutineScope.launch {
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 400
                        0f at 0
                        -20f at 50
                        20f at 100
                        -15f at 150
                        15f at 200
                        -10f at 250
                        10f at 300
                        0f at 400
                    }
                )
            }
        }
    }

    // Dynamic background color transition
    val cardBgColor by animateColorAsState(
        targetValue = when {
            isSelected && isCorrect == true -> Color(0xFF4CAF50)  // Correct -> Green
            isSelected && isCorrect == false -> Color(0xFFF44336) // Wrong -> Red
            isSelected -> Color(0xFF6200EE)                       // Selected -> Purple
            else -> Color(0xFFF5F5F5)                             // Default -> Soft Gray
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "CardBgColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Black,
        label = "TextColor"
    )

    // Spring Bouncy Scale Effect
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "CardScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer {
                translationX = shakeOffset.value
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}