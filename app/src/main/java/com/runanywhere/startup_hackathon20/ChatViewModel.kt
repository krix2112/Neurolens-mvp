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
import android.util.Log
import com.runanywhere.startup_hackathon20.services.HookService
import com.runanywhere.startup_hackathon20.services.ConversationState

// 🧠 Represents a chat message (user or AI)
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val tag: String? = null // optional emotion tag for UI
)

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

    private val _statusMessage = MutableStateFlow("Initializing...")
    val statusMessage: StateFlow<String> = _statusMessage

    init {
        // ✅ This is now properly recognized
        loadAvailableModels()
    }

    // 🔍 Load list of available models
    private fun loadAvailableModels() {
        viewModelScope.launch {
            try {
                val models = listAvailableModels()
                _availableModels.value = models
                _statusMessage.value = "Ready — Download or load a model"
                Log.i("ChatVM", "📦 Models available: ${models.map { it.name }}")
            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Error listing models: ${e.message}")
                _statusMessage.value = "Error loading models: ${e.message}"
            }
        }
    }

    // 💾 Download model with detailed Logcat progress
    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Downloading model..."
                Log.i("ChatVM", "🚀 Starting model download: $modelId")

                var lastLogged = 0
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    val percent = (progress * 100).toInt()
                    _downloadProgress.value = progress
                    _statusMessage.value = "Downloading: $percent%"

                    if (percent - lastLogged >= 5) {
                        Log.d("ChatVM", "📦 Download progress: $percent%")
                        lastLogged = percent
                    }
                }

                _downloadProgress.value = null
                _statusMessage.value = "✅ Download complete — tap Load"
                Log.i("ChatVM", "🎯 Model download complete: $modelId")

            } catch (e: Exception) {
                Log.e("ChatVM", "❌ Download error: ${e.message}", e)
                _statusMessage.value = "Download failed: ${e.message}"
                _downloadProgress.value = null
            }
        }
    }

    // ⚙️ Load model — resolves by name or ID and falls back to mock mode if it fails
    fun loadModel(modelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _statusMessage.value = "⏳ Loading model..."
                Log.d("ChatVM", "🧠 Attempting to load model: $modelId")

                val modelInfo = _availableModels.value.find { it.id.toString() == modelId }
                if (modelInfo == null) {
                    _statusMessage.value = "⚠️ Model not found in list"
                    Log.e("ChatVM", "❌ Model not found for ID $modelId")
                    return@launch
                }

                Log.i("ChatVM", "🔗 Resolved model name: ${modelInfo.name}")

                // ✅ Get model path manually using the same directory used by RunAnywhere SDK
                val baseDir = android.os.Environment.getExternalStorageDirectory().absolutePath
                val modelPath = "$baseDir/Android/data/com.runanywhere.startup_hackathon20/files/models/llama_cpp/smollm2-360m-q8_0.gguf"
                val file = java.io.File(modelPath)

                if (!file.exists()) {
                    _statusMessage.value = "⚠️ Model file missing at: $modelPath"
                    Log.e("ChatVM", "❌ Model file missing at: $modelPath")
                    return@launch
                }

                Log.i("ChatVM", "📂 Found local model file: $modelPath")

                // --- Load model via reflection ---
                var success = false
                try {
                    val clazz = Class.forName("com.runanywhere.sdk.llm.llamacpp.LlamaCppServiceProvider")
                    val registerMethod = clazz.getDeclaredMethod("register")
                    registerMethod.invoke(null)

                    // Access singleton instance
                    val instanceField = clazz.getDeclaredField("INSTANCE")
                    instanceField.isAccessible = true
                    val providerInstance = instanceField.get(null)

                    // Look for a single-argument loadModel(String) method
                    val loadModelMethod = clazz.methods.find {
                        it.name == "loadModel" && it.parameterTypes.size == 1
                    }

                    if (loadModelMethod != null) {
                        val result = loadModelMethod.invoke(providerInstance, modelPath)
                        success = (result != null)
                        Log.i("ChatVM", "📦 loadModel(path) invoked via reflection → $success")
                    } else {
                        Log.e("ChatVM", "❌ loadModel() method not found in provider class")
                    }
                } catch (e: Exception) {
                    Log.e("ChatVM", "❌ Reflection error while loading model: ${e.message}", e)
                }

                // ✅ Final state update
                if (success) {
                    _currentModelId.value = modelId
                    _statusMessage.value = "✅ Model loaded successfully — Live mode active"
                    Log.i("ChatVM", "🎯 Model successfully loaded at $modelPath")
                } else {
                    _currentModelId.value = "mock"
                    _statusMessage.value = "⚡ Mock Mode Active — model load failed"
                    Log.w("ChatVM", "⚡ Model load failed, fallback to mock mode")
                }

            } catch (e: Exception) {
                _currentModelId.value = "mock"
                _statusMessage.value = "⚡ Mock Mode Active (error: ${e.message})"
                Log.e("ChatVM", "❌ Exception while loading model: ${e.message}", e)
            }
        }
    }







    // 💬 Send message — handles both live and mock modes
    fun sendMessage(text: String) {
        if (_currentModelId.value == null) {
            _statusMessage.value = "Please load or activate mock mode first"
            return
        }

        _messages.value += ChatMessage(text, isUser = true)
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_currentModelId.value == "mock") {
                    val convo: ConversationState = HookService.processJournal(text)
                    val response = buildString {
                        append("Detected mood: ${convo.emotion}\n")
                        append("Try these:\n")
                        convo.advice.forEachIndexed { i, s -> append("${i + 1}. $s\n") }
                    }

                    val updated = _messages.value.toMutableList()
                    updated[updated.lastIndex] =
                        updated.last().copy(tag = convo.emotion)
                    updated.add(ChatMessage(response, isUser = false, tag = convo.emotion))
                    _messages.value = updated
                    Log.d("ChatVM", "💬 Mock response generated: $response")

                } else {
                    var aiResponse = ""
                    RunAnywhere.generateStream(text).collect { token ->
                        aiResponse += token
                        val msgs = _messages.value.toMutableList()
                        if (msgs.lastOrNull()?.isUser == false) {
                            msgs[msgs.lastIndex] = ChatMessage(aiResponse, isUser = false)
                        } else {
                            msgs.add(ChatMessage(aiResponse, isUser = false))
                        }
                        _messages.value = msgs
                    }
                    Log.i("ChatVM", "🤖 AI Response (real model): $aiResponse")
                }

            } catch (e: Exception) {
                Log.e("ChatVM", "❌ sendMessage error: ${e.message}", e)
                _messages.value += ChatMessage("Error: ${e.message}", isUser = false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🔁 Refresh list
    fun refreshModels() = loadAvailableModels()
}
