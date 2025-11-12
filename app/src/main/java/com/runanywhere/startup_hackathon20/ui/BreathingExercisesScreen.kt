package com.runanywhere.startup_hackathon20.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.ui.theme.*
import kotlinx.coroutines.delay

data class BreathingExercise(
    val name: String,
    val description: String,
    val benefit: String,
    val inhale: Int,
    val hold1: Int,
    val exhale: Int,
    val hold2: Int,
    val cycles: Int,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExercisesScreen(
    onBackClick: () -> Unit
) {
    var selectedExercise by remember { mutableStateOf<BreathingExercise?>(null) }

    val exercises = remember {
        listOf(
            BreathingExercise(
                name = "4-7-8 Breathing",
                description = "Calming technique for anxiety and sleep",
                benefit = "Reduces anxiety, promotes sleep",
                inhale = 4, hold1 = 7, exhale = 8, hold2 = 0, cycles = 4,
                color = CalmBlue
            ),
            BreathingExercise(
                name = "Box Breathing",
                description = "Used by Navy SEALs for focus",
                benefit = "Enhances focus and calm",
                inhale = 4, hold1 = 4, exhale = 4, hold2 = 4, cycles = 5,
                color = CalmTeal
            ),
            BreathingExercise(
                name = "Physiological Sigh",
                description = "Quick stress reset in 30 seconds",
                benefit = "Rapid stress relief",
                inhale = 2, hold1 = 0, exhale = 5, hold2 = 0, cycles = 3,
                color = SoftPurple
            ),
            BreathingExercise(
                name = "Alternate Nostril",
                description = "Balance left and right brain",
                benefit = "Balances energy, reduces anxiety",
                inhale = 4, hold1 = 4, exhale = 4, hold2 = 0, cycles = 5,
                color = WarmGreen
            )
        )
    }

    if (selectedExercise != null) {
        BreathingExercisePlayer(
            exercise = selectedExercise!!,
            onClose = { selectedExercise = null }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Breathing Exercises",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                "Calm your nervous system",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeutralGray
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = CalmBlue
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = DarkGray
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(LightGray),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    InfoCard()
                }

                items(exercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onClick = { selectedExercise = exercise }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CalmTeal, LightCalmBlue)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "🫁 Why Breathing Works",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Controlled breathing activates your parasympathetic nervous system, telling your body it's safe to relax. Just 2-3 minutes can reduce stress hormones significantly.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: BreathingExercise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(exercise.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = exercise.color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = NeutralGray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = WarmGreen.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = exercise.benefit,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmGreen
                    )
                }
            }

            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Start",
                tint = exercise.color,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExercisePlayer(
    exercise: BreathingExercise,
    onClose: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf("Tap Start") }
    var currentCycle by remember { mutableStateOf(0) }

    val animatedSize by animateFloatAsState(
        targetValue = if (currentPhase.contains("Inhale")) 200f else 100f,
        animationSpec = tween(
            durationMillis = when {
                currentPhase.contains("Inhale") -> exercise.inhale * 1000
                currentPhase.contains("Exhale") -> exercise.exhale * 1000
                else -> 1000
            }
        ),
        label = "breathing_animation"
    )

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            currentCycle = 0
            repeat(exercise.cycles) { cycle ->
                currentCycle = cycle + 1

                // Inhale
                currentPhase = "Inhale (${exercise.inhale}s)"
                delay(exercise.inhale * 1000L)

                // Hold 1
                if (exercise.hold1 > 0) {
                    currentPhase = "Hold (${exercise.hold1}s)"
                    delay(exercise.hold1 * 1000L)
                }

                // Exhale
                currentPhase = "Exhale (${exercise.exhale}s)"
                delay(exercise.exhale * 1000L)

                // Hold 2
                if (exercise.hold2 > 0) {
                    currentPhase = "Hold (${exercise.hold2}s)"
                    delay(exercise.hold2 * 1000L)
                }
            }
            currentPhase = "Complete! 🎉"
            isPlaying = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(exercise.name) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = exercise.color,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(exercise.color.copy(alpha = 0.3f), LightGray)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Breathing Circle Animation
                Box(
                    modifier = Modifier
                        .size(animatedSize.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    exercise.color.copy(alpha = 0.6f),
                                    exercise.color.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentPhase,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Cycle Counter
                if (isPlaying) {
                    Text(
                        text = "Cycle $currentCycle / ${exercise.cycles}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = DarkGray
                        )
                    )
                }

                // Control Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!isPlaying) {
                        Button(
                            onClick = { isPlaying = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = exercise.color
                            ),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start", fontSize = 18.sp)
                        }
                    } else {
                        Button(
                            onClick = {
                                isPlaying = false
                                currentPhase = "Paused"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AngryRed
                            ),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Stop", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}