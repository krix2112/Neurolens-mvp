# 🎨 FRONTEND INTEGRATION GUIDE

**Complete guide for integrating your UI with the enhanced backend**

---

## 🚀 Quick Start

### 1. Pull Latest Code

```bash
git pull origin main
# or download latest from GitHub
```

### 2. Verify Build

```bash
cd K:/demo
./gradlew clean assembleDebug
```

### 3. Test Backend

1. Install on device: `./gradlew installDebug`
2. Open app
3. Click "Activate Mock Mode"
4. Type: "I'm stressed"
5. ✅ Verify you get detailed ChatGPT-like response

---

## 📋 What's Available for You

### ✅ ChatViewModel (Main Interface)

Location: `app/src/main/java/com/runanywhere/startup_hackathon20/ChatViewModel.kt`

**Key Functions You'll Use:**

```kotlin
// 1. Send user message (supports ALL features)
viewModel.sendMessage(text: String)

// 2. Observe chat messages
val messages by viewModel.messages.collectAsState()

// 3. Observe loading state
val isLoading by viewModel.isLoading.collectAsState()

// 4. Observe status messages
val statusMessage by viewModel.statusMessage.collectAsState()

// 5. Activate Mock Mode (for demo)
viewModel.activateMockMode()

// 6. Configure Ollama (optional)
viewModel.configureOllama(serverUrl, modelName)

// 7. Check model source
val modelSource by viewModel.modelSource.collectAsState()
// Returns: LOCAL_GGUF, OLLAMA, or MOCK
```

---

## 🎯 Integration Steps

### Step 1: Connect to ViewModel

```kotlin
@Composable
fun YourMainScreen() {
    val viewModel: ChatViewModel = viewModel()
    
    // Collect states
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    
    // Your UI here
}
```

### Step 2: Send Messages

```kotlin
// For text input
TextField(
    value = inputText,
    onValueChange = { inputText = it }
)

Button(
    onClick = {
        viewModel.sendMessage(inputText)
        inputText = "" // Clear input
    },
    enabled = !isLoading && inputText.isNotBlank()
) {
    Text("Send")
}
```

### Step 3: Display Messages

```kotlin
LazyColumn {
    items(messages) { message ->
        MessageBubble(
            text = message.text,
            isUser = message.isUser,
            emotion = message.tag // Optional emotion tag
        )
    }
}
```

---

## 🎨 UI Features You Can Build

### 1. **Voice Journal Screen**

```kotlin
@Composable
fun VoiceJournalScreen(viewModel: ChatViewModel) {
    Button(onClick = {
        viewModel.sendMessage("Tell me about voice journaling")
    }) {
        Text("🎙️ Voice Journal Guide")
    }
}
```

### 2. **Task Manager Screen**

```kotlin
@Composable
fun TaskScreen(viewModel: ChatViewModel) {
    Button(onClick = {
        viewModel.sendMessage("Help me organize my tasks")
    }) {
        Text("📝 Organize Tasks")
    }
}
```

### 3. **Breather/Meditation Screen**

```kotlin
@Composable
fun BreatherScreen(viewModel: ChatViewModel) {
    Button(onClick = {
        viewModel.sendMessage("I need a breathing exercise")
    }) {
        Text("🫁 Breathing Exercise")
    }
}
```

### 4. **Reminder Setup Screen**

```kotlin
@Composable
fun RemindersScreen(viewModel: ChatViewModel) {
    Button(onClick = {
        viewModel.sendMessage("Help me set up reminders for self-care")
    }) {
        Text("⏰ Setup Reminders")
    }
}
```

---

## 📊 Data Models

### ChatMessage

```kotlin
data class ChatMessage(
    val text: String,        // Message content
    val isUser: Boolean,     // true = user, false = AI
    val tag: String? = null  // Emotion tag (Anxious, Happy, etc.)
)
```

### ModelSource (Enum)

```kotlin
enum class ModelSource {
    LOCAL_GGUF,  // Local model
    OLLAMA,      // Ollama server
    MOCK         // Mock mode (demo)
}
```

---

## 🔥 Example Integration Patterns

### Pattern 1: Simple Chat Interface

```kotlin
@Composable
fun SimpleChatScreen(viewModel: ChatViewModel = viewModel()) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        // Status Bar
        StatusBar(viewModel)
        
        // Messages List
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
        
        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                enabled = !isLoading
            )
            
            Spacer(Modifier.width(8.dp))
            
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                enabled = !isLoading && inputText.isNotBlank()
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun StatusBar(viewModel: ChatViewModel) {
    val statusMessage by viewModel.statusMessage.collectAsState()
    
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = statusMessage,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val color = if (message.isUser) 
        MaterialTheme.colorScheme.primaryContainer
    else 
        MaterialTheme.colorScheme.secondaryContainer
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = color),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = if (message.isUser) "You" else "AI",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                // Optional: Show emotion tag
                message.tag?.let { emotion ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "😊 $emotion",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
```

### Pattern 2: Feature Navigation

```kotlin
@Composable
fun HomeScreen(
    viewModel: ChatViewModel,
    onNavigateToChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Journal
        FeatureCard(
            title = "🎙️ Voice Journal",
            description = "Express yourself naturally",
            onClick = {
                viewModel.sendMessage("Tell me about voice journaling")
                onNavigateToChat()
            }
        )
        
        // Task Manager
        FeatureCard(
            title = "📝 Tasks",
            description = "Organize your to-do list",
            onClick = {
                viewModel.sendMessage("Help me organize my tasks")
                onNavigateToChat()
            }
        )
        
        // Breather
        FeatureCard(
            title = "🫁 Breather",
            description = "Calming breathing exercises",
            onClick = {
                viewModel.sendMessage("I need a breathing exercise")
                onNavigateToChat()
            }
        )
        
        // Reminders
        FeatureCard(
            title = "⏰ Reminders",
            description = "Setup self-care reminders",
            onClick = {
                viewModel.sendMessage("Help me set up reminders")
                onNavigateToChat()
            }
        )
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

### Pattern 3: Quick Actions

```kotlin
@Composable
fun QuickActionsRow(viewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionChip(
            text = "😰 Feeling Anxious",
            onClick = { viewModel.sendMessage("I'm feeling stressed and anxious") }
        )
        
        QuickActionChip(
            text = "😊 Feeling Happy",
            onClick = { viewModel.sendMessage("I'm feeling happy today!") }
        )
        
        QuickActionChip(
            text = "📝 Organize Tasks",
            onClick = { viewModel.sendMessage("Help me organize my tasks") }
        )
        
        QuickActionChip(
            text = "🫁 Breathing",
            onClick = { viewModel.sendMessage("breathing exercise") }
        )
    }
}

@Composable
fun QuickActionChip(text: String, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) }
    )
}
```

---

## 🎯 Supported Message Types

The backend intelligently detects what you're asking for:

### Emotional Support

```kotlin
"I'm stressed"
"I feel sad"
"I'm so happy today!"
"I'm angry at my team"
"I'm exhausted"
"Feeling grateful"
```

### Task Management

```kotlin
"Help me organize my tasks"
"I have too many things to do"
"Can you help with my to-do list?"
```

### Reminders

```kotlin
"I need reminders for self-care"
"Help me set up reminders"
"Remind me to take breaks"
```

### Voice Journal

```kotlin
"Tell me about voice journaling"
"How do I voice journal?"
"Is voice or text better?"
```

### Breathing

```kotlin
"I need a breathing exercise"
"Help me calm down"
"Meditation techniques"
```

### Goals

```kotlin
"Help me achieve my goals"
"I want to start exercising"
"How do I set goals that stick?"
```

---

## ✅ Pre-Integration Checklist

Before you start integrating:

- [ ] Backend builds successfully (`./gradlew assembleDebug`)
- [ ] App installs on device (`./gradlew installDebug`)
- [ ] Mock Mode activates (click button, see status)
- [ ] Test message works (type "I'm stressed", get detailed response)
- [ ] Responses are ChatGPT-quality (detailed, 500+ words)

---

## 🐛 Troubleshooting

### Issue: ViewModel not found

**Solution:** Import correctly:

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.ChatViewModel
```

### Issue: Messages not updating

**Solution:** Use `collectAsState()`:

```kotlin
val messages by viewModel.messages.collectAsState()
```

### Issue: Responses are short

**Solution:** Make sure Mock Mode is active:

```kotlin
LaunchedEffect(Unit) {
    viewModel.activateMockMode()
}
```

### Issue: App crashes

**Solution:** Check these:

1. Mock Mode activated before sending messages
2. ViewModel scoped correctly
3. All imports present

---

## 📞 API Reference

### ChatViewModel Functions

| Function | Parameters | Returns | Description |
|----------|-----------|---------|-------------|
| `sendMessage()` | text: String | Unit | Send user message |
| `activateMockMode()` | - | Unit | Activate mock mode |
| `toggleMockMode()` | - | Unit | Toggle mock on/off |
| `configureOllama()` | serverUrl, modelName | Unit | Setup Ollama |
| `loadModel()` | modelId: String | Unit | Load local model |
| `downloadModel()` | modelId: String | Unit | Download model |

### StateFlows (Observable)

| Property | Type | Description |
|----------|------|-------------|
| `messages` | StateFlow<List<ChatMessage>> | Chat history |
| `isLoading` | StateFlow<Boolean> | Loading state |
| `statusMessage` | StateFlow<String> | Status text |
| `isMockMode` | StateFlow<Boolean> | Mock mode active |
| `modelSource` | StateFlow<ModelSource> | Current source |
| `currentModelId` | StateFlow<String?> | Active model |

---

## 🎨 Recommended UI Flow

```
App Launch
    ↓
Home Screen (Voice Journal, Tasks, Breather, Reminders)
    ↓
User selects feature → Pre-fills message → Chat Screen
    ↓
User types/edits message → Sends
    ↓
AI responds with detailed, helpful content
    ↓
User continues conversation OR returns to Home
```

---

## 🚀 Quick Integration Example

```kotlin
// 1. In your main composable
@Composable
fun App() {
    val viewModel: ChatViewModel = viewModel()
    var currentScreen by remember { mutableStateOf("home") }
    
    // Activate mock mode on launch
    LaunchedEffect(Unit) {
        viewModel.activateMockMode()
    }
    
    when (currentScreen) {
        "home" -> HomeScreen(
            viewModel = viewModel,
            onNavigateToChat = { currentScreen = "chat" }
        )
        "chat" -> ChatScreen(
            viewModel = viewModel,
            onBack = { currentScreen = "home" }
        )
    }
}

// 2. Minimal Chat Screen
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var input by remember { mutableStateOf("") }
    
    Column(Modifier.fillMaxSize()) {
        // Back button
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back")
        }
        
        // Messages
        LazyColumn(Modifier.weight(1f)) {
            items(messages) { ChatBubble(it) }
        }
        
        // Input
        Row(Modifier.padding(16.dp)) {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    viewModel.sendMessage(input)
                    input = ""
                },
                enabled = !isLoading && input.isNotBlank()
            ) {
                Text("Send")
            }
        }
    }
}
```

---

## ✅ Final Checklist

Before submitting:

- [ ] Your UI connects to ChatViewModel
- [ ] Messages display correctly
- [ ] Loading states handled
- [ ] Mock Mode activates on app launch
- [ ] Home screen has Voice Journal, Tasks, Breather, Reminders
- [ ] Chat screen works smoothly
- [ ] Tested on actual device
- [ ] No crashes or errors

---

## 🎉 You're Ready!

**Total Integration Time: 30-60 minutes**

The backend is fully functional with ChatGPT-quality responses. Just connect your beautiful UI to
it!

**Questions?** Check the existing `MainActivity.kt` for a working reference implementation.

Good luck! 🚀
