package com.runanywhere.startup_hackathon20.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun GoalSettingScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    var goalInput by remember { mutableStateOf("") }
    var showAIHelper by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Goal Setting",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "SMART goals that stick",
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
                actions = {
                    IconButton(onClick = { showAIHelper = !showAIHelper }) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Info",
                            tint = MotivatedOrange
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
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(MotivatedOrange, WarmOrange)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "🎯 SMART Goals Framework",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Set goals that are Specific, Measurable, Achievable, Relevant, and Time-bound.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }

            item {
                SMARTFrameworkCard()
            }

            item {
                TipsCard()
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "💬 Need Help?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Ask our AI assistant: 'Help me create a goal for...'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NeutralGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { /* Navigate to emotional journal */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MotivatedOrange
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Talk to AI Assistant")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SMARTFrameworkCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SMARTItem("S", "Specific", "Clear and well-defined goal", CalmBlue)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SMARTItem("M", "Measurable", "Track progress with numbers", WarmGreen)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SMARTItem("A", "Achievable", "Realistic and attainable", MotivatedOrange)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SMARTItem("R", "Relevant", "Aligns with your values", SoftPurple)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SMARTItem("T", "Time-bound", "Has a clear deadline", AngryRed)
        }
    }
}

@Composable
fun SMARTItem(letter: String, title: String, description: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
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
fun TipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = WarmGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "💡 Quick Tips",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WarmGreen
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Start small - Tiny habits compound over time\n" +
                        "• Track daily - Yes/No tracking works best\n" +
                        "• Be flexible - Adjust goals as needed\n" +
                        "• Celebrate wins - Acknowledge progress",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}