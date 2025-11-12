package com.runanywhere.startup_hackathon20.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.ui.theme.*

data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFeature: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val features = remember {
        listOf(
            FeatureItem(
                title = "Emotional Journal",
                description = "Process feelings with AI guidance",
                icon = Icons.Default.FavoriteBorder,
                gradientColors = listOf(CalmBlue, LightCalmBlue),
                route = "emotional_journal"
            ),
            FeatureItem(
                title = "Task Management",
                description = "Organize life with smart strategies",
                icon = Icons.Default.CheckCircle,
                gradientColors = listOf(WarmGreen, LightGreen),
                route = "task_management"
            ),
            FeatureItem(
                title = "Breathing Exercises",
                description = "Calm your mind with guided breathing",
                icon = Icons.Default.Refresh,
                gradientColors = listOf(CalmTeal, Color(0xFFB2DFDB)),
                route = "breathing_exercises"
            ),
            FeatureItem(
                title = "Goal Setting",
                description = "Achieve dreams with proven frameworks",
                icon = Icons.Default.Star,
                gradientColors = listOf(MotivatedOrange, Color(0xFFFFE0B2)),
                route = "goal_setting"
            ),
            FeatureItem(
                title = "Smart Reminders",
                description = "Self-care reminders that actually work",
                icon = Icons.Default.Notifications,
                gradientColors = listOf(SoftPurple, Color(0xFFE1BEE7)),
                route = "smart_reminders"
            ),
            FeatureItem(
                title = "Voice Journaling",
                description = "Express yourself naturally",
                icon = Icons.Default.Phone,
                gradientColors = listOf(GratefulPink, Color(0xFFF8BBD9)),
                route = "voice_journaling"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Neurolens",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CalmBlue
                            )
                        )
                        Text(
                            "Your Mental Health Companion",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeutralGray
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
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
            // Welcome Section
            item {
                WelcomeCard()
            }

            // Quick Stats Section
            item {
                QuickStatsCard()
            }

            // Features Grid
            item {
                Text(
                    text = "Choose Your Journey",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(600.dp), // Fixed height for grid in LazyColumn
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(features) { feature ->
                        FeatureCard(
                            feature = feature,
                            onClick = { onNavigateToFeature(feature.route) }
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CalmBlue, SoftPurple)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Welcome back! 🌟",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "How are you feeling today? Let's explore what you need.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}

@Composable
fun QuickStatsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Favorite,
                    value = "3",
                    label = "Check-ins",
                    color = GratefulPink
                )
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    value = "5",
                    label = "Tasks Done",
                    color = WarmGreen
                )
                StatItem(
                    icon = Icons.Default.Refresh,
                    value = "2",
                    label = "Breathing",
                    color = CalmTeal
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = NeutralGray
            )
        )
    }
}

@Composable
fun FeatureCard(
    feature: FeatureItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = feature.gradientColors
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        text = feature.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = feature.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        ),
                        textAlign = TextAlign.Start,
                        maxLines = 2
                    )
                }
            }
        }
    }
}