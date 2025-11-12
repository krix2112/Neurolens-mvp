package com.runanywhere.startup_hackathon20.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.ChatViewModel
import com.runanywhere.startup_hackathon20.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceJournalingScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    var isRecording by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Voice Journaling",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Express yourself naturally",
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(GratefulPink, SoftPurple)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "🎙️ Why Voice Journaling?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Speaking feels more natural than writing. Express complex emotions easily without the pressure to 'write well'.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }

            item {
                RecordingCard(
                    isRecording = isRecording,
                    onToggleRecording = { isRecording = !isRecording }
                )
            }

            item {
                BenefitsCard()
            }

            item {
                PromptsCard()
            }
        }
    }
}

@Composable
fun RecordingCard(
    isRecording: Boolean,
    onToggleRecording: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isRecording) AngryRed.copy(alpha = 0.1f) else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        if (isRecording)
                            Brush.radialGradient(
                                colors = listOf(AngryRed, AngryRed.copy(alpha = 0.5f))
                            )
                        else
                            Brush.radialGradient(
                                colors = listOf(GratefulPink, GratefulPink.copy(alpha = 0.5f))
                            )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onToggleRecording,
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        if (isRecording) Icons.Default.Close else Icons.Default.Phone,
                        contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isRecording) "Recording... Tap to stop" else "Tap to start recording",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isRecording) AngryRed else DarkGray
                )
            )

            if (isRecording) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    color = AngryRed
                )
            }
        }
    }
}

@Composable
fun BenefitsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "✨ Benefits of Voice Journaling",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitItem(
                icon = "🗣️",
                title = "More Natural",
                description = "Speaking feels easier than writing"
            )
            Spacer(modifier = Modifier.height(8.dp))
            BenefitItem(
                icon = "⚡",
                title = "Faster Processing",
                description = "Capture thoughts before they slip away"
            )
            Spacer(modifier = Modifier.height(8.dp))
            BenefitItem(
                icon = "💭",
                title = "Emotional Depth",
                description = "Your voice carries emotions text can't"
            )
            Spacer(modifier = Modifier.height(8.dp))
            BenefitItem(
                icon = "🎯",
                title = "Problem Solving",
                description = "Talking through issues brings clarity"
            )
        }
    }
}

@Composable
fun BenefitItem(icon: String, title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.width(40.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = NeutralGray
                )
            )
        }
    }
}

@Composable
fun PromptsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = GratefulPink.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "💡 Prompts to Get Started",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = GratefulPink
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Right now I'm feeling...\n" +
                        "• Today I'm grateful for...\n" +
                        "• What's bothering me is...\n" +
                        "• I'm proud of myself for...\n" +
                        "• Tomorrow I want to...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}