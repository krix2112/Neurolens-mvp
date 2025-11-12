package com.runanywhere.startup_hackathon20.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import com.runanywhere.startup_hackathon20.ChatMessage
import com.runanywhere.startup_hackathon20.ChatViewModel
import com.runanywhere.startup_hackathon20.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionalJournalScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    // Collect states
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isMockMode by viewModel.isMockMode.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var inputText by remember { mutableStateOf("") }

    // Auto-activate mock mode if not already active
    LaunchedEffect(Unit) {
        if (!isMockMode) {
            viewModel.activateMockMode()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Emotional Journal",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Express yourself, get AI guidance",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGray)
        ) {
            // Status indicator
            if (!isMockMode) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = WarmOrange.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = statusMessage,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentOrange
                    )
                }
            }

            // Messages List
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Welcome message if no messages
                if (messages.isEmpty()) {
                    item {
                        WelcomeJournalCard()
                    }
                    item {
                        EmotionalPromptCards()
                    }
                }

                items(
                    items = messages,
                    key = { message -> "${message.text.hashCode()}_${message.isUser}" }
                ) { message ->
                    EmotionalMessageBubble(message)
                }

                if (isLoading) {
                    item {
                        LoadingMessageBubble()
                    }
                }
            }

            // Auto-scroll to bottom when new messages arrive
            LaunchedEffect(messages.size, isLoading) {
                if (messages.isNotEmpty() || isLoading) {
                    delay(100)
                    listState.animateScrollToItem(
                        if (isLoading) messages.size + 1 else messages.size - 1
                    )
                }
            }

            // Input Field
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "How are you feeling today?",
                                color = NeutralGray
                            )
                        },
                        enabled = !isLoading,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = CalmBlue,
                            unfocusedIndicatorColor = NeutralGray.copy(alpha = 0.3f)
                        ),
                        maxLines = 3
                    )

                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isLoading) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = if (isLoading || inputText.isBlank()) NeutralGray else CalmBlue,
                        contentColor = Color.White
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeJournalCard() {
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
                        colors = listOf(CalmBlue, LightCalmBlue)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Welcome to your safe space 💙",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Share your thoughts and feelings. I'm here to listen and provide supportive guidance based on proven therapeutic approaches.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}

@Composable
fun EmotionalPromptCards() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Need help getting started? Try one of these:",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = DarkGray
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val prompts = listOf(
            "I'm feeling stressed about work...",
            "I had a great day today because...",
            "I'm worried about the future...",
            "I feel overwhelmed with everything..."
        )

        prompts.forEach { prompt ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Add functionality to pre-fill input */ },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Text(
                    text = prompt,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = CalmBlue
                )
            }
        }
    }
}

@Composable
fun EmotionalMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) CalmBlue else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (!message.isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🤖 AI Assistant",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = CalmBlue
                        )
                        if (message.tag != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            EmotionChip(emotion = message.tag)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isUser) Color.White else DarkGray
                )
            }
        }
    }
}

@Composable
fun EmotionChip(emotion: String) {
    val emotionColor = when (emotion.lowercase()) {
        "anxious" -> AnxiousYellow
        "sad" -> SadBlue
        "happy" -> HappyYellow
        "angry" -> AngryRed
        "tired" -> TiredPurple
        "motivated" -> MotivatedOrange
        "calm" -> CalmTeal
        "grateful" -> GratefulPink
        else -> NeutralGray
    }

    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = emotionColor.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = emotion,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = emotionColor
        )
    }
}

@Composable
fun LoadingMessageBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 120.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    val alpha by animateFloatAsState(
                        targetValue = if (System.currentTimeMillis() / 300 % 3 == index.toLong()) 1f else 0.3f,
                        label = "typing_animation"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                CalmBlue.copy(alpha = alpha),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
        }
    }
}