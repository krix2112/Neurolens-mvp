package com.runanywhere.startup_hackathon20

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import android.util.Log
import com.runanywhere.startup_hackathon20.services.HookService
import com.runanywhere.startup_hackathon20.services.ConversationState
import com.runanywhere.startup_hackathon20.services.OllamaService

// 🧠 Represents a chat message (user or AI)
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val tag: String? = null // optional emotion tag for UI
)

// Model source type
enum class ModelSource {
    LOCAL_GGUF,  // Local GGUF models via RunAnywhere SDK
    OLLAMA,      // Ollama server models
    MOCK         // Mock mode for testing
}

class ChatViewModel : ViewModel() {

    // --- Reactive state ---
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    private val _statusMessage = MutableStateFlow("Initializing SDK...")
    val statusMessage: StateFlow<String> = _statusMessage

    private val _isMockMode = MutableStateFlow(false)
    val isMockMode: StateFlow<Boolean> = _isMockMode
    
    private val _modelSource = MutableStateFlow(ModelSource.MOCK)
    val modelSource: StateFlow<ModelSource> = _modelSource
    
    private val _ollamaModels = MutableStateFlow<List<com.runanywhere.startup_hackathon20.services.OllamaModel>>(emptyList())
    val ollamaModels: StateFlow<List<com.runanywhere.startup_hackathon20.services.OllamaModel>> = _ollamaModels
    
    private val _ollamaConnected = MutableStateFlow(false)
    val ollamaConnected: StateFlow<Boolean> = _ollamaConnected

    // Track last update time to throttle UI updates
    private var lastMessageUpdateTime = 0L

    init {
        Log.d("ChatVM", "✅ ViewModel initialized - UI can display now")

        // CRITICAL FIX: Use Dispatchers.IO for all collect operations
        // This ensures they never block the main thread or UI initialization

        // Coroutine 1: Wait for SDK initialization (non-blocking)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatVM", "⚙️ Background: Listening for SDK initialization...")
            MyApplication.sdkInitialized.collect { initialized ->
                if (initialized) {
                    Log.i("ChatVM", "✅ Background: SDK initialized - loading models list")
                    delay(500) // Small delay to ensure UI is ready
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Loading models..."
                    }
                    loadAvailableModels()
                }
            }
        }

        // Coroutine 2: Wait for model ready (separate from SDK init, non-blocking)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatVM", "⚙️ Background: Listening for model ready state...")
            MyApplication.modelReady.collect { ready ->
                if (ready) {
                    Log.i("ChatVM", "✅ Background: Model ready - activating live mode")
                    delay(300) // Small delay to prevent UI jank
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Model ready - Live mode active"
                        _currentModelId.value = "live"
                        _modelSource.value = ModelSource.LOCAL_GGUF
                    }
                }
            }
        }
        
        // Coroutine 3: Check for initialization errors
        viewModelScope.launch(Dispatchers.IO) {
            MyApplication.initError.collect { error ->
                if (error != null) {
                    Log.e("ChatVM", "❌ Initialization error: $error")
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Error: $error - Use Mock/Ollama mode"
                    }
                }
            }
        }
    }

    // Load list of available models
    private fun loadAvailableModels() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("ChatVM", "Fetching available models...")
                val models = withTimeout(10000) {
                    listAvailableModels()
                }
                _availableModels.value = models

                withContext(Dispatchers.Main) {
                    if (_currentModelId.value == null) {
                        _statusMessage.value = "Ready — Download/load model or use Ollama"
                    }
                }

                Log.i("ChatVM", "Found ${models.size} models: ${models.map { it.name }}")
            } catch (e: Exception) {
                Log.e("ChatVM", "Error listing models: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Error loading models: ${e.message}"
                }
            }
        }
    }

    // Download model with detailed Logcat progress
    fun downloadModel(modelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Downloading model..."
                }
                Log.i("ChatVM", "⬇️ Starting model download: $modelId")

                var lastLogged = 0
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    val percent = (progress * 100).toInt()

                    withContext(Dispatchers.Main) {
                        _downloadProgress.value = progress
                        _statusMessage.value = "Downloading: $percent%"
                    }

                    if (percent - lastLogged >= 5) {
                        Log.d("ChatVM", "📦 Download progress: $percent%")
                        lastLogged = percent
                    }
                }

                Log.i("ChatVM", "✅ Model download complete: $modelId")

                // CRITICAL: Scan for downloaded models immediately after download
                Log.d("ChatVM", "🔄 Scanning for downloaded models after download...")
                try {
                    withTimeout(5000) {
                        RunAnywhere.scanForDownloadedModels()
                    }
                    Log.i("ChatVM", "✅ Scan complete - model is now available")
                } catch (e: Exception) {
                    Log.w("ChatVM", "⚠️ Scan timeout: ${e.message}")
                }

                // Refresh the models list so UI shows updated status
                loadAvailableModels()

                withContext(Dispatchers.Main) {
                    _downloadProgress.value = null
                    _statusMessage.value = "Download complete — tap Load to use model"
                }

            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Download error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Download failed: ${e.message}"
                    _downloadProgress.value = null
                }
            }
        }
    }

    // Load model — uses AGGRESSIVE direct file loading with automatic fallback
    fun loadModel(modelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Loading model..."
                }
                Log.d("ChatVM", "Starting AGGRESSIVE model load for: $modelId")
                
                // Check if device can handle model loading
                if (!MyApplication.canHandleModelLoading(MyApplication.instance)) {
                    Log.e("ChatVM", "Device cannot handle model loading")
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Insufficient resources - Use Ollama or Mock mode"
                        _currentModelId.value = "mock"
                        _isMockMode.value = true
                        _modelSource.value = ModelSource.MOCK
                    }
                    return@launch
                }

                // CRITICAL: Scan for downloaded models first with timeout
                Log.d("ChatVM", "Scanning for downloaded models before loading...")
                try {
                    withTimeout(5000) {
                        RunAnywhere.scanForDownloadedModels()
                    }
                    Log.i("ChatVM", "Scan complete")
                } catch (e: Exception) {
                    Log.w("ChatVM", "Scan timeout: ${e.message}")
                }

                // Get model info
                val models = try {
                    withTimeout(10000) {
                        listAvailableModels()
                    }
                } catch (e: Exception) {
                    Log.e("ChatVM", "Timeout listing models: ${e.message}")
                    emptyList()
                }
                _availableModels.value = models
                Log.d("ChatVM", "Available models after scan: ${models.size}")

                val modelInfo = models.find { it.id.toString() == modelId }
                if (modelInfo == null) {
                    Log.e("ChatVM", "Model not found for ID: $modelId")
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Model not found - Use Ollama or Mock mode"
                        activateMockMode()
                    }
                    return@launch
                }

                Log.i(
                    "ChatVM",
                    "Resolved model: ${modelInfo.name}, Downloaded: ${modelInfo.isDownloaded}"
                )

                // Check if model file exists
                if (!modelInfo.isDownloaded) {
                    Log.w("ChatVM", "Model not downloaded yet: ${modelInfo.name}")
                    withContext(Dispatchers.Main) {
                        _statusMessage.value = "Please download the model first"
                    }
                    return@launch
                }

                Log.i("ChatVM", "Model is downloaded, starting AGGRESSIVE load...")

                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Loading model... (this may take a few minutes)"
                }

                // ===== NEW: DIRECT FILE PATH LOADING (Most reliable) =====
                var loaded = false
                var attemptCount = 0

                // Build direct file path
                val modelDir = MyApplication.instance.getExternalFilesDir(null)
                val modelFileName = when (modelInfo.name) {
                    "SmolLM2 360M Q8_0" -> "smollm2-360m-q8_0.gguf"
                    "Qwen 2.5 0.5B Instruct Q6_K" -> "qwen2.5-0.5b-instruct-q6_k.gguf"
                    else -> null
                }

                // STRATEGY 1: Direct file path (MOST RELIABLE)
                if (!loaded && modelFileName != null && modelDir != null) {
                    attemptCount++
                    val modelFilePath = "${modelDir}/models/llama_cpp/$modelFileName"
                    val file = java.io.File(modelFilePath)

                    if (file.exists() && file.length() > 10_000_000) {
                        try {
                            Log.d(
                                "ChatVM",
                                "ATTEMPT $attemptCount: Direct file load: $modelFilePath"
                            )
                            Log.d("ChatVM", "File size: ${file.length() / 1024 / 1024}MB")

                            withContext(Dispatchers.Main) {
                                _statusMessage.value =
                                    "Loading from file... (${file.length() / 1024 / 1024}MB)"
                            }

                            // Try loading - this is blocking and can take MINUTES
                            loaded = RunAnywhere.loadModel(modelFilePath)

                            if (loaded) {
                                Log.i("ChatVM", "SUCCESS: Model loaded via direct file path!")
                            } else {
                                Log.w("ChatVM", "Direct file load returned false")
                            }
                        } catch (e: Exception) {
                            Log.e("ChatVM", "Direct file load failed: ${e.message}", e)
                        }
                    } else {
                        Log.w("ChatVM", "File not found or too small: $modelFilePath")
                    }
                }

                // STRATEGY 2: Load by model name
                if (!loaded) {
                    attemptCount++
                    try {
                        Log.d("ChatVM", "ATTEMPT $attemptCount: Load by name: ${modelInfo.name}")
                        loaded = RunAnywhere.loadModel(modelInfo.name)
                        if (loaded) {
                            Log.i("ChatVM", "SUCCESS: Model loaded by name!")
                        }
                    } catch (e: Exception) {
                        Log.w("ChatVM", "Load by name failed: ${e.message}")
                    }
                }

                // STRATEGY 3: Load by ID
                if (!loaded) {
                    attemptCount++
                    try {
                        Log.d("ChatVM", "ATTEMPT $attemptCount: Load by ID: $modelId")
                        loaded = RunAnywhere.loadModel(modelId)
                        if (loaded) {
                            Log.i("ChatVM", "SUCCESS: Model loaded by ID!")
                        }
                    } catch (e: Exception) {
                        Log.w("ChatVM", "Load by ID failed: ${e.message}")
                    }
                }

                // Final state update
                withContext(Dispatchers.Main) {
                    if (loaded) {
                        _currentModelId.value = "live"
                        _statusMessage.value = "Live mode active - Local GGUF model loaded"
                        _isMockMode.value = false
                        _modelSource.value = ModelSource.LOCAL_GGUF
                        Log.i("ChatVM", "Model successfully loaded: ${modelInfo.name}")
                    } else {
                        Log.e(
                            "ChatVM",
                            "All loading strategies failed after $attemptCount attempts"
                        )
                        Log.e("ChatVM", "Falling back to Mock Mode")
                        _currentModelId.value = "mock"
                        _statusMessage.value =
                            "Model load failed - Mock Mode active (Use Ollama for real AI)"
                        _isMockMode.value = true
                        _modelSource.value = ModelSource.MOCK
                    }
                }

            } catch (e: Exception) {
                Log.e("ChatVM", "Critical error loading model: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _currentModelId.value = "mock"
                    _statusMessage.value = "Error: ${e.message} - Use Ollama or Mock mode"
                    _isMockMode.value = true
                    _modelSource.value = ModelSource.MOCK
                }
            }
        }
    }
    
    // === OLLAMA INTEGRATION ===
    
    /**
     * Configure Ollama server connection
     */
    fun configureOllama(serverUrl: String, modelName: String = "llama2") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("ChatVM", "🔧 Configuring Ollama: $serverUrl, model: $modelName")
                OllamaService.configure(serverUrl, modelName)
                
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Testing Ollama connection..."
                }
                
                val connected = OllamaService.testConnection()
                
                withContext(Dispatchers.Main) {
                    _ollamaConnected.value = connected
                    if (connected) {
                        _statusMessage.value = "Ollama connected ✓"
                        Log.i("ChatVM", "✅ Ollama connected successfully")
                        // Load available models
                        loadOllamaModels()
                    } else {
                        _statusMessage.value = "Ollama connection failed"
                        Log.e("ChatVM", "❌ Ollama connection failed")
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Ollama configuration error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _ollamaConnected.value = false
                    _statusMessage.value = "Ollama error: ${e.message}"
                }
            }
        }
    }
    
    /**
     * Load available Ollama models
     */
    fun loadOllamaModels() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Loading Ollama models..."
                }
                
                val models = OllamaService.listModels()
                
                withContext(Dispatchers.Main) {
                    _ollamaModels.value = models
                    _statusMessage.value = "Found ${models.size} Ollama models"
                    Log.i("ChatVM", "📋 Loaded ${models.size} Ollama models")
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Error loading Ollama models: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Error loading Ollama models"
                }
            }
        }
    }
    
    /**
     * Activate Ollama model
     */
    fun activateOllamaModel(modelName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("ChatVM", "🚀 Activating Ollama model: $modelName")
                
                // Configure with selected model
                OllamaService.configure(OllamaService.baseUrl, modelName)
                
                withContext(Dispatchers.Main) {
                    _currentModelId.value = modelName
                    _modelSource.value = ModelSource.OLLAMA
                    _isMockMode.value = false
                    _statusMessage.value = "Ollama model active: $modelName ✓"
                    Log.i("ChatVM", "✅ Ollama model activated: $modelName")
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Error activating Ollama model: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Error: ${e.message}"
                }
            }
        }
    }
    
    /**
     * Pull an Ollama model from library
     */
    fun pullOllamaModel(modelName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Pulling Ollama model..."
                }
                Log.i("ChatVM", "⬇️ Pulling Ollama model: $modelName")
                
                OllamaService.pullModel(modelName).collect { progress ->
                    withContext(Dispatchers.Main) {
                        _downloadProgress.value = progress.progress
                        _statusMessage.value = "Pulling: ${progress.status}"
                    }
                    
                    if (progress.complete) {
                        Log.i("ChatVM", "✅ Ollama model pull complete")
                        loadOllamaModels() // Refresh list
                        withContext(Dispatchers.Main) {
                            _downloadProgress.value = null
                            _statusMessage.value = "Pull complete ✓"
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Ollama pull error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _statusMessage.value = "Pull failed: ${e.message}"
                    _downloadProgress.value = null
                }
            }
        }
    }

    // Send message — handles local GGUF, Ollama, and mock modes
    fun sendMessage(text: String) {
        if (_currentModelId.value == null && !_isMockMode.value) {
            _statusMessage.value = "Please load a model or activate mock mode first"
            return
        }

        _messages.value += ChatMessage(text, isUser = true)
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (_modelSource.value) {
                    ModelSource.MOCK -> handleMockMessage(text)
                    ModelSource.LOCAL_GGUF -> handleLocalGGUFMessage(text)
                    ModelSource.OLLAMA -> handleOllamaMessage(text)
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "sendMessage error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _messages.value += ChatMessage("Error: ${e.message}", isUser = false)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
    
    private suspend fun handleMockMessage(text: String) {
        // Mock mode - use HookService
        Log.d("ChatVM", "Using mock mode for message: $text")

        // Small delay to simulate processing (prevents UI freeze)
        delay(300)

        val convo: ConversationState = HookService.processJournal(text)

        // Use detailed response if available, otherwise fallback to short format
        val response = convo.detailedResponse ?: buildString {
            append("Detected mood: ${convo.emotion}\n\n")
            append("Try these:\n")
            convo.advice.forEachIndexed { i, s -> append("${i + 1}. $s\n") }
        }

        withContext(Dispatchers.Main) {
            val updated = _messages.value.toMutableList()
            updated[updated.lastIndex] =
                updated.last().copy(tag = convo.emotion)
            updated.add(ChatMessage(response, isUser = false, tag = convo.emotion))
            _messages.value = updated
        }
        Log.d("ChatVM", "Mock response generated: ${response.take(100)}...")
    }
    
    private suspend fun handleLocalGGUFMessage(text: String) {
        // Live mode - use real model
        Log.i("ChatVM", "Generating AI response using local GGUF model for: $text")
        var aiResponse = ""

        try {
            RunAnywhere.generateStream(text).collect { token ->
                aiResponse += token

                // Throttle UI updates to every 500ms to prevent jank
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastMessageUpdateTime >= 500) {
                    withContext(Dispatchers.Main) {
                        val msgs = _messages.value.toMutableList()
                        if (msgs.lastOrNull()?.isUser == false) {
                            msgs[msgs.lastIndex] = ChatMessage(aiResponse, isUser = false)
                        } else {
                            msgs.add(ChatMessage(aiResponse, isUser = false))
                        }
                        _messages.value = msgs
                    }
                    lastMessageUpdateTime = currentTime
                }
            }

            // CRITICAL: Always show the final complete message
            withContext(Dispatchers.Main) {
                val msgs = _messages.value.toMutableList()
                if (msgs.lastOrNull()?.isUser == false) {
                    msgs[msgs.lastIndex] = ChatMessage(aiResponse, isUser = false)
                } else {
                    msgs.add(ChatMessage(aiResponse, isUser = false))
                }
                _messages.value = msgs
            }

            Log.i("ChatVM", "AI response complete (${aiResponse.length} chars)")
        } catch (e: Exception) {
            Log.e("ChatVM", "❌ Local GGUF generation error: ${e.message}", e)
            withContext(Dispatchers.Main) {
                _messages.value += ChatMessage("Error: ${e.message}", isUser = false)
            }
        }
    }
    
    private suspend fun handleOllamaMessage(text: String) {
        // Ollama mode - use Ollama server
        Log.i("ChatVM", "Generating AI response using Ollama for: $text")
        var aiResponse = ""

        try {
            OllamaService.generateStream(text).collect { token ->
                aiResponse += token

                // Throttle UI updates to every 500ms to prevent jank
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastMessageUpdateTime >= 500) {
                    withContext(Dispatchers.Main) {
                        val msgs = _messages.value.toMutableList()
                        if (msgs.lastOrNull()?.isUser == false) {
                            msgs[msgs.lastIndex] = ChatMessage(aiResponse, isUser = false)
                        } else {
                            msgs.add(ChatMessage(aiResponse, isUser = false))
                        }
                        _messages.value = msgs
                    }
                    lastMessageUpdateTime = currentTime
                }
            }

            // CRITICAL: Always show the final complete message
            withContext(Dispatchers.Main) {
                val msgs = _messages.value.toMutableList()
                if (msgs.lastOrNull()?.isUser == false) {
                    msgs[msgs.lastIndex] = ChatMessage(aiResponse, isUser = false)
                } else {
                    msgs.add(ChatMessage(aiResponse, isUser = false))
                }
                _messages.value = msgs
            }

            Log.i("ChatVM", "Ollama response complete (${aiResponse.length} chars)")
        } catch (e: Exception) {
            Log.e("ChatVM", "❌ Ollama generation error: ${e.message}", e)
            withContext(Dispatchers.Main) {
                _messages.value += ChatMessage("Error: ${e.message}", isUser = false)
            }
        }
    }

    // Refresh models list
    fun refreshModels() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatVM", "🔄 Refreshing models list...")

            // Always scan before refreshing to ensure we have latest state
            try {
                withTimeout(5000) {
                    RunAnywhere.scanForDownloadedModels()
                }
                Log.i("ChatVM", "✅ Scan complete")
            } catch (e: Exception) {
                Log.w("ChatVM", "⚠️ Scan failed: ${e.message}")
            }

            loadAvailableModels()
        }
    }

    // Manually activate mock mode (lightweight, no model loading needed)
    fun activateMockMode() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("ChatVM", "🎭 Activating Mock Mode (no model loading)")
            withContext(Dispatchers.Main) {
                _currentModelId.value = "mock"
                _statusMessage.value = "Mock Mode Active — No model loaded"
                _isMockMode.value = true
                _modelSource.value = ModelSource.MOCK
            }
            Log.i("ChatVM", "✅ Mock mode activated - ready for testing")
        }
    }

    fun toggleMockMode() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("ChatVM", "🔄 Toggling Mock Mode")
            withContext(Dispatchers.Main) {
                _isMockMode.value = !_isMockMode.value
                if (_isMockMode.value) {
                    _currentModelId.value = "mock"
                    _statusMessage.value = "Mock Mode Active — No model loaded"
                    _modelSource.value = ModelSource.MOCK
                } else {
                    _currentModelId.value = null
                    _statusMessage.value = "Ready — Download/load model or use Ollama"
                    _modelSource.value = ModelSource.MOCK
                }
            }
            Log.i("ChatVM", "✅ Mock mode toggled")
        }
    }
}
