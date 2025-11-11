package com.runanywhere.startup_hackathon20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.ui.theme.Startup_hackathon20Theme
import android.util.Log
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "🚀 NeuroLens MVP starting")
        enableEdgeToEdge()
        setContent {
            Startup_hackathon20Theme {
                NeuroLensApp()
            }
        }
        Log.i("MainActivity", "✅ NeuroLens UI displayed")
    }
}

// Navigation destinations
enum class Screen {
    HOME, VOICE_JOURNAL, TASKS, BREATHER, REMINDERS, CHAT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeuroLensApp(viewModel: ChatViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var title by remember { mutableStateOf("NeuroLens") }

    // Auto-activate mock mode on startup
    LaunchedEffect(Unit) {
        viewModel.activateMockMode()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (currentScreen != Screen.HOME) {
                        IconButton(onClick = {
                            currentScreen = Screen.HOME
                            title = "NeuroLens"
                        }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                },
                actions = {
                    if (currentScreen == Screen.CHAT) {
                        TextButton(onClick = { viewModel.activateMockMode() }) {
                            Text("Mock Mode")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (currentScreen) {
            Screen.HOME -> {
                title = "NeuroLens"
                HomeScreen(
                    modifier = Modifier.padding(padding),
                    onNavigate = { screen ->
                        currentScreen = screen
                        title = when (screen) {
                            Screen.VOICE_JOURNAL -> "Voice Journal"
                            Screen.TASKS -> "Task Manager"
                            Screen.BREATHER -> "Breather"
                            Screen.REMINDERS -> "Reminders"
                            Screen.CHAT -> "AI Chat"
                            else -> "NeuroLens"
                        }
                    },
                    viewModel = viewModel
                )
            }

            Screen.VOICE_JOURNAL -> VoiceJournalScreen(
                modifier = Modifier.padding(padding),
                onStartChat = {
                    currentScreen = Screen.CHAT
                    title = "AI Chat"
                    viewModel.sendMessage("Tell me about voice journaling")
                }
            )

            Screen.TASKS -> TasksScreen(
                modifier = Modifier.padding(padding),
                onStartChat = {
                    currentScreen = Screen.CHAT
                    title = "AI Chat"
                    viewModel.sendMessage("Help me organize my tasks")
                }
            )

            Screen.BREATHER -> BreatherScreen(
                modifier = Modifier.padding(padding),
                onStartChat = {
                    currentScreen = Screen.CHAT
                    title = "AI Chat"
                    viewModel.sendMessage("I need a breathing exercise")
                }
            )

            Screen.REMINDERS -> RemindersScreen(
                modifier = Modifier.padding(padding),
                onStartChat = {
                    currentScreen = Screen.CHAT
                    title = "AI Chat"
                    viewModel.sendMessage("Help me set up reminders for self-care")
                }
            )

            Screen.CHAT -> ChatScreen(
                modifier = Modifier.padding(padding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    viewModel: ChatViewModel
) {
    val statusMessage by viewModel.statusMessage.collectAsState()
    val isMockMode by viewModel.isMockMode.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard()
        }

        item {
            StatusCard(statusMessage = statusMessage, isMockMode = isMockMode)
        }

        item {
            Text(
                text = "Features",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    title = "Voice Journal",
                    icon = Icons.Default.Phone,
                    description = "Express yourself naturally",
                    onClick = { onNavigate(Screen.VOICE_JOURNAL) },
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    title = "Tasks",
                    icon = Icons.Default.List,
                    description = "Organize your life",
                    onClick = { onNavigate(Screen.TASKS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    title = "Breather",
                    icon = Icons.Default.Favorite,
                    description = "Calm your mind",
                    onClick = { onNavigate(Screen.BREATHER) },
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    title = "Reminders",
                    icon = Icons.Default.Notifications,
                    description = "Stay on track",
                    onClick = { onNavigate(Screen.REMINDERS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            QuickActionsCard(
                onAction = { message ->
                    viewModel.sendMessage(message)
                    onNavigate(Screen.CHAT)
                }
            )
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🧠 Welcome to NeuroLens",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your AI-powered mental wellness companion",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StatusCard(statusMessage: String, isMockMode: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isMockMode)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isMockMode) Icons.Default.Star else Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isMockMode) "Demo Mode Active" else "AI Ready",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickActionsCard(onAction: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))

            val quickActions = listOf(
                "😰 I'm feeling stressed" to "Get instant stress relief",
                "😊 I'm feeling great today!" to "Celebrate your mood",
                "🎯 Help me focus" to "Get productivity tips",
                "💤 I need better sleep" to "Improve sleep habits"
            )

            quickActions.forEach { (message, description) ->
                OutlinedButton(
                    onClick = { onAction(message) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun VoiceJournalScreen(
    modifier: Modifier = Modifier,
    onStartChat: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FeatureHeader(
                title = "🎙️ Voice Journal",
                description = "Express your thoughts and feelings naturally through voice"
            )
        }

        item {
            InfoCard(
                title = "Why Voice Journaling?",
                content = """
                • More natural than typing
                • Capture emotions in your voice
                • Process thoughts faster
                • Review your emotional journey
                """.trimIndent()
            )
        }

        item {
            InfoCard(
                title = "How to Get Started",
                content = """
                1. Find a quiet, comfortable space
                2. Start with "Today I'm feeling..."
                3. Speak for 2-5 minutes freely
                4. Don't worry about perfection
                5. Review your entries weekly
                """.trimIndent()
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🚀 Try Voice Journaling",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get personalized guidance on voice journaling techniques and best practices.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartChat,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Voice Journal Guide")
                    }
                }
            }
        }
    }
}

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    onStartChat: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FeatureHeader(
                title = "📝 Task Manager",
                description = "Organize and prioritize your daily tasks effectively"
            )
        }

        item {
            InfoCard(
                title = "Smart Task Organization",
                content = """
                • Break large tasks into smaller steps
                • Prioritize by importance and urgency
                • Set realistic deadlines
                • Track your progress
                """.trimIndent()
            )
        }

        item {
            InfoCard(
                title = "Productivity Techniques",
                content = """
                • Pomodoro Technique (25 min focus blocks)
                • Time blocking for important tasks
                • Energy-based scheduling
                • Regular breaks and rewards
                """.trimIndent()
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎯 Organize Your Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get AI-powered strategies to organize your tasks and boost productivity.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartChat,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Task Organization Help")
                    }
                }
            }
        }
    }
}

@Composable
fun BreatherScreen(
    modifier: Modifier = Modifier,
    onStartChat: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FeatureHeader(
                title = "🫁 Breather",
                description = "Calm your mind with guided breathing exercises"
            )
        }

        item {
            InfoCard(
                title = "Breathing Techniques Available",
                content = """
                • 4-7-8 Breathing (for anxiety and sleep)
                • Box Breathing (for focus and calm)
                • Physiological Sigh (quick stress relief)
                • Alternate Nostril (for balance)
                """.trimIndent()
            )
        }

        item {
            InfoCard(
                title = "Benefits of Breathing Exercises",
                content = """
                • Reduces stress and anxiety
                • Improves focus and concentration
                • Helps with sleep quality
                • Regulates nervous system
                • Lowers blood pressure
                """.trimIndent()
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🧘 Start Breathing Exercise",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get guided breathing exercises tailored to your current needs.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartChat,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Breathing Guide")
                    }
                }
            }
        }
    }
}

@Composable
fun RemindersScreen(
    modifier: Modifier = Modifier,
    onStartChat: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FeatureHeader(
                title = "⏰ Reminders",
                description = "Stay consistent with your self-care and wellness goals"
            )
        }

        item {
            InfoCard(
                title = "Effective Reminder Types",
                content = """
                • Self-care check-ins
                • Hydration reminders
                • Movement breaks
                • Emotional wellness checks
                • Medication and vitamins
                """.trimIndent()
            )
        }

        item {
            InfoCard(
                title = "Best Practices",
                content = """
                • Make reminders specific and actionable
                • Set them at natural transition points
                • Use positive, encouraging language
                • Adjust frequency based on your needs
                """.trimIndent()
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔔 Setup Smart Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get personalized reminder strategies that actually work for your lifestyle.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartChat,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Reminder Setup Help")
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureHeader(
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: String
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
            )
        }
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    // Collect states efficiently
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val availableModels by viewModel.availableModels.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val isMockMode by viewModel.isMockMode.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showModelSelector by remember { mutableStateOf(false) }

    val isInputEnabled = !isLoading && (currentModelId != null || isMockMode)
    val isSendEnabled =
        !isLoading && inputText.isNotBlank() && (currentModelId != null || isMockMode)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Status bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
                downloadProgress?.let { progress ->
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }

        // Model selector (collapsible)
        if (showModelSelector) {
            ModelSelector(
                models = availableModels,
                currentModelId = currentModelId,
                onDownload = { modelId -> viewModel.downloadModel(modelId) },
                onLoad = { modelId -> viewModel.loadModel(modelId) },
                onRefresh = { viewModel.refreshModels() },
                isMockMode = isMockMode,
                onToggleMockMode = { viewModel.toggleMockMode() }
            )
        }

        // Messages List
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = messages,
                key = { message -> "${message.text.hashCode()}_${message.isUser}" }
            ) { message ->
                MessageBubble(message)
            }
        }

        // Auto-scroll to bottom when new messages arrive
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                delay(100)
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                enabled = isInputEnabled,
                singleLine = true
            )

            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                enabled = isSendEnabled
            ) {
                Text("Send")
            }
        }
    }
}

// ... existing composables like MessageBubble, ModelSelector, etc. ...

@Composable
fun ModelSelector(
    models: List<com.runanywhere.sdk.models.ModelInfo>,
    currentModelId: String?,
    onDownload: (String) -> Unit,
    onLoad: (String) -> Unit,
    onRefresh: () -> Unit,
    isMockMode: Boolean,
    onToggleMockMode: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Models",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onRefresh) {
                    Text("Refresh")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mock Mode Toggle - Always visible at top
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMockMode)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mock Mode",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Test without loading model",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isMockMode,
                        onCheckedChange = { onToggleMockMode() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (models.isEmpty()) {
                Text(
                    text = "No models available. Initializing...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 250.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = models,
                        key = { model -> model.id }
                    ) { model ->
                        ModelItem(
                            model = model,
                            isLoaded = model.id == currentModelId,
                            onDownload = { onDownload(model.id) },
                            onLoad = { onLoad(model.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModelItem(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isLoaded)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = model.name,
                style = MaterialTheme.typography.titleSmall
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ Currently Loaded",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                        enabled = !model.isDownloaded
                    ) {
                        Text(
                            text = if (model.isDownloaded) "Downloaded" else "Download",
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = onLoad,
                        modifier = Modifier.weight(1f),
                        enabled = model.isDownloaded && !isLoaded
                    ) {
                        Text("Load", maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (message.isUser)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = if (message.isUser) "You" else "AI",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Startup_hackathon20Theme {
        NeuroLensApp()
    }
}