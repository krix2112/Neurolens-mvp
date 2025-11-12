package com.runanywhere.startup_hackathon20.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
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
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    // Collect states
    val isMockMode by viewModel.isMockMode.collectAsState()
    val ollamaConnected by viewModel.ollamaConnected.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    // Change default URL to correct Android emulator localhost address
    var ollamaUrl by remember { mutableStateOf("http://10.0.2.2:11434") }
    var selectedModel by remember { mutableStateOf("llama2") }
    var showModelList by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // App Mode Section
            AppModeSection(
                isMockMode = isMockMode,
                onToggleMockMode = { viewModel.toggleMockMode() }
            )

            // Ollama Instructions Card
            OllamaInstructionsCard()

            // Ollama Configuration Section
            OllamaConfigSection(
                ollamaUrl = ollamaUrl,
                selectedModel = selectedModel,
                ollamaConnected = ollamaConnected,
                onUrlChange = { ollamaUrl = it },
                onModelChange = { selectedModel = it },
                onConnect = {
                    viewModel.configureOllama(ollamaUrl, selectedModel)
                },
                onTestConnection = {
                    viewModel.configureOllama(ollamaUrl, selectedModel)
                }
            )

            // Status Section
            StatusSection(
                statusMessage = statusMessage,
                ollamaConnected = ollamaConnected
            )

            // Local Models Section (for future)
            LocalModelsSection(viewModel = viewModel)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AppModeSection(
    isMockMode: Boolean,
    onToggleMockMode: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "App Mode",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mock Mode",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isMockMode) "Using simulated AI responses" else "Using live AI models",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeutralGray
                    )
                }

                Switch(
                    checked = isMockMode,
                    onCheckedChange = { onToggleMockMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CalmBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = NeutralGray
                    )
                )
            }
        }
    }
}

@Composable
fun OllamaInstructionsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGray.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📚 Ollama Setup Instructions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Follow these steps to connect to Ollama:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            InstructionStep(
                number = "1",
                text = "Install Ollama on your computer from ollama.ai"
            )
            InstructionStep(
                number = "2",
                text = "Open terminal and run: ollama serve"
            )
            InstructionStep(
                number = "3",
                text = "Pull a model: ollama pull llama2"
            )
            InstructionStep(
                number = "4",
                text = "For emulator use: http://10.0.2.2:11434\nFor real device use: http://YOUR_PC_IP:11434"
            )
            InstructionStep(
                number = "5",
                text = "Tap 'Connect to Ollama' below"
            )
        }
    }
}

@Composable
fun InstructionStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CalmBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = NeutralGray,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OllamaConfigSection(
    ollamaUrl: String,
    selectedModel: String,
    ollamaConnected: Boolean,
    onUrlChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onConnect: () -> Unit,
    onTestConnection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ollama Configuration",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (ollamaConnected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Connected",
                        tint = WarmGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Server URL Input
            Text(
                text = "Server URL",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = ollamaUrl,
                onValueChange = onUrlChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("http://10.0.2.2:11434") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray.copy(alpha = 0.3f),
                    unfocusedContainerColor = LightGray.copy(alpha = 0.3f),
                    focusedIndicatorColor = CalmBlue,
                    unfocusedIndicatorColor = NeutralGray.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Model Selection
            Text(
                text = "Model",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = selectedModel,
                onValueChange = onModelChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("llama2, mistral, codellama, etc.") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray.copy(alpha = 0.3f),
                    unfocusedContainerColor = LightGray.copy(alpha = 0.3f),
                    focusedIndicatorColor = CalmBlue,
                    unfocusedIndicatorColor = NeutralGray.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Connect Button
            Button(
                onClick = onConnect,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CalmBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (ollamaConnected) "Reconnect to Ollama" else "Connect to Ollama",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun StatusSection(
    statusMessage: String,
    ollamaConnected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (ollamaConnected) LightGreen else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = if (ollamaConnected) DeepGreen else DarkGray
            )
        }
    }
}

@Composable
fun LocalModelsSection(viewModel: ChatViewModel = viewModel()) {
    // Collect states
    val availableModels by viewModel.availableModels.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📦 Local GGUF Models",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Warning about loading time
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MotivatedOrange.copy(alpha = 0.1f))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "⚠️ Important",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MotivatedOrange
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Local models require:\n• 2-5 minutes to load\n• 500MB+ RAM\n• ARM64 device\n\nRecommended: Use Ollama or Mock Mode for better performance",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Available Models:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Find models in the available list
            val smolModel = availableModels.find { it.name.contains("SmolLM2", ignoreCase = true) }
            val qwenModel = availableModels.find { it.name.contains("Qwen", ignoreCase = true) }

            // SmolLM2 Model Card
            ModelDownloadCard(
                name = "SmolLM2 360M Q8_0",
                size = "~360 MB",
                modelInfo = smolModel,
                isDownloading = downloadProgress != null,
                downloadProgress = downloadProgress,
                onDownload = {
                    smolModel?.let { viewModel.downloadModel(it.id.toString()) }
                },
                onLoad = {
                    smolModel?.let { viewModel.loadModel(it.id.toString()) }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Qwen Model Card
            ModelDownloadCard(
                name = "Qwen 2.5 0.5B Instruct Q6_K",
                size = "~500 MB",
                modelInfo = qwenModel,
                isDownloading = downloadProgress != null,
                downloadProgress = downloadProgress,
                onDownload = {
                    qwenModel?.let { viewModel.downloadModel(it.id.toString()) }
                },
                onLoad = {
                    qwenModel?.let { viewModel.loadModel(it.id.toString()) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Refresh button
            OutlinedButton(
                onClick = { viewModel.refreshModels() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CalmBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "🔄 Refresh Model List",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ModelDownloadCard(
    name: String,
    size: String,
    modelInfo: com.runanywhere.sdk.models.ModelInfo?,
    isDownloading: Boolean,
    downloadProgress: Float?,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LightGray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = size,
                        style = MaterialTheme.typography.bodySmall,
                        color = NeutralGray
                    )
                }

                // Status badge
                if (modelInfo != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    modelInfo.isDownloaded -> WarmGreen.copy(alpha = 0.2f)
                                    else -> NeutralGray.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = when {
                                modelInfo.isDownloaded -> "✓ Downloaded"
                                else -> "Not Downloaded"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = when {
                                modelInfo.isDownloaded -> WarmGreen
                                else -> NeutralGray
                            }
                        )
                    }
                }
            }

            // Show download progress if downloading
            if (isDownloading && downloadProgress != null) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { downloadProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CalmBlue,
                    trackColor = LightGray,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Downloading: ${(downloadProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = CalmBlue
                )
            }

            // Action buttons
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Download button
                if (modelInfo?.isDownloaded == false) {
                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CalmBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isDownloading
                    ) {
                        Text(
                            text = if (isDownloading) "Downloading..." else "⬇️ Download",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Load button (only shown if downloaded)
                if (modelInfo?.isDownloaded == true) {
                    Button(
                        onClick = onLoad,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarmGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "🚀 Load Model",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModelInfoCard(name: String, size: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LightGray.copy(alpha = 0.3f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$size • $status",
                style = MaterialTheme.typography.bodySmall,
                color = NeutralGray
            )
        }
    }
}