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
import com.runanywhere.startup_hackathon20.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartRemindersScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Smart Reminders",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Self-care that works",
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
                                    colors = listOf(SoftPurple, CalmBlue)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "⏰ Effective Reminders",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Set up actionable reminders that actually help you follow through.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }

            item {
                ReminderCategoriesCard()
            }

            item {
                TimingTipsCard()
            }

            item {
                ExamplesCard()
            }
        }
    }
}

@Composable
fun ReminderCategoriesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "📋 Reminder Categories",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            CategoryItem("🧘 Self-Care", "Drink water, stretch, take breaks", CalmTeal)
            Spacer(modifier = Modifier.height(8.dp))
            CategoryItem("💊 Health", "Medication, vitamins, exercise", WarmGreen)
            Spacer(modifier = Modifier.height(8.dp))
            CategoryItem("📝 Tasks", "Deadlines, meetings, calls", MotivatedOrange)
            Spacer(modifier = Modifier.height(8.dp))
            CategoryItem("😊 Emotional", "How are you feeling?", GratefulPink)
        }
    }
}

@Composable
fun CategoryItem(title: String, description: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
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
fun TimingTipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MotivatedOrange.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⏰ Best Times to Set Reminders",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MotivatedOrange
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Morning: 30 min after waking\n" +
                        "• Work breaks: 10am, 2pm, 4pm\n" +
                        "• Evening: Before wind-down time\n" +
                        "• Transition points: Natural breaks in your day",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ExamplesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "✅ Good vs ❌ Bad Reminders",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            ExampleRow(
                bad = "Exercise",
                good = "Put on workout clothes and do 10 jumping jacks"
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ExampleRow(
                bad = "Meditate",
                good = "Sit comfortably and take 5 deep breaths"
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ExampleRow(
                bad = "Be productive",
                good = "Start the first task on your list for 25 minutes"
            )
        }
    }
}

@Composable
fun ExampleRow(bad: String, good: String) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Text("❌ ", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = bad,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AngryRed.copy(alpha = 0.7f)
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Top) {
            Text("✅ ", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = good,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WarmGreen
                )
            )
        }
    }
}