package com.runanywhere.startup_hackathon20

import android.app.Application
import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.data.models.SDKEnvironment
import com.runanywhere.sdk.public.extensions.addModelFromURL
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.llm.llamacpp.LlamaCppServiceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import kotlin.math.roundToInt
import android.os.Build
import android.app.ActivityManager
import android.content.Context

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set

        // Application-scoped coroutine context
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // Track SDK initialization state
        private val _sdkInitialized = MutableStateFlow(false)
        val sdkInitialized: StateFlow<Boolean> = _sdkInitialized

        // Track model loading state
        private val _modelReady = MutableStateFlow(false)
        val modelReady: StateFlow<Boolean> = _modelReady

        // Track initialization error
        private val _initError = MutableStateFlow<String?>(null)
        val initError: StateFlow<String?> = _initError

        // Check if device is ARM64 compatible
        fun isARM64Compatible(): Boolean {
            val supportedAbis = Build.SUPPORTED_ABIS
            Log.d("MyApp", "📱 Supported ABIs: ${supportedAbis.joinToString()}")
            return supportedAbis.any { it.contains("arm64-v8a") || it.contains("arm64") }
        }

        // Check available memory
        fun getAvailableMemoryMB(context: Context): Long {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            return memoryInfo.availMem / (1024 * 1024)
        }

        // Check if device can handle model loading
        fun canHandleModelLoading(context: Context): Boolean {
            val availableMemory = getAvailableMemoryMB(context)
            val isArm64 = isARM64Compatible()

            Log.i("MyApp", "💾 Available memory: ${availableMemory}MB")
            Log.i("MyApp", "🏗️ ARM64 compatible: $isArm64")

            // Need at least 500MB free and ARM64
            return availableMemory >= 500 && isArm64
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Log.i("MyApp", "🚀 Application.onCreate() starting")

        // Check device compatibility
        if (!isARM64Compatible()) {
            Log.e("MyApp", "❌ Device is not ARM64 compatible - models may not work")
            _initError.value = "Device not compatible (ARM64 required)"
        }

        val availableMemory = getAvailableMemoryMB(this)
        Log.i("MyApp", "💾 Available memory: ${availableMemory}MB")

        if (availableMemory < 500) {
            Log.w("MyApp", "⚠️ Low memory detected - may affect model loading")
        }

        // ⚡ CRITICAL: Launch ALL initialization asynchronously
        // This ensures onCreate() returns immediately and UI can start
        appScope.launch {
            Log.i("MyApp", "⚙️ Background initialization continuing...")
            try {
                initializeSDKAsync()
            } catch (e: Exception) {
                Log.e("MyApp", "❌ Fatal initialization error: ${e.message}", e)
                _initError.value = "Initialization failed: ${e.message}"
                _sdkInitialized.value = true // Still mark as done so UI isn't stuck
            }
        }

        Log.i("MyApp", "✅ Application.onCreate() completed - UI will start now")
    }

    /**
     * Complete async initialization - runs entirely off main thread
     * UI will start immediately while this runs in background
     */
    private suspend fun initializeSDKAsync() {
        try {
            Log.i("MyApp", "⚙️ Starting async SDK initialization...")

            // Step 1: Initialize SDK core with timeout
            withTimeout(10000) { // 10 second timeout
                RunAnywhere.initialize(
                    context = this@MyApplication,
                    apiKey = "dev",
                    environment = SDKEnvironment.DEVELOPMENT
                )
            }
            Log.i("MyApp", "✅ SDK core initialized")

            // Small delay to prevent choking the system
            kotlinx.coroutines.delay(150)

            // Step 2: Register service provider
            LlamaCppServiceProvider.register()
            Log.i("MyApp", "✅ LlamaCpp service provider registered")

            kotlinx.coroutines.delay(150)

            // Step 3: Register model metadata
            registerModels()
            Log.i("MyApp", "✅ Models registered")

            kotlinx.coroutines.delay(150)

            // Mark SDK as initialized (UI can now show models list)
            _sdkInitialized.value = true

            // Step 4: Scan for existing models (CRITICAL - must happen before loading)
            try {
                withTimeout(5000) { // 5 second timeout
                    RunAnywhere.scanForDownloadedModels()
                }
                Log.i("MyApp", "✅ Scanned for downloaded models")
            } catch (e: Exception) {
                Log.w("MyApp", "⚠️ Model scan timeout or error: ${e.message}")
            }

            // Step 5: Check if model file exists (but DON'T auto-download or auto-load)
            // This reduces startup load and prevents crashes from heavy operations
            val modelFile = checkModelFileExists()

            if (modelFile != null && modelFile.exists()) {
                Log.i("MyApp", "✅ Model file found: ${modelFile.absolutePath} (${modelFile.length() / 1024 / 1024} MB)")
                Log.i("MyApp", "💡 Model ready to load - user can load it from UI when needed")
            } else {
                Log.i("MyApp", "ℹ️ No model file found - user can download from UI")
            }

            Log.i("MyApp", "🎉 SDK initialization complete - app ready to use")

        } catch (e: Exception) {
            Log.e("MyApp", "❌ SDK initialization failed: ${e.message}", e)
            _initError.value = "SDK initialization failed: ${e.message}"
            _sdkInitialized.value = true // Still mark as done so UI isn't stuck
        }
    }

    /**
     * Check if model file exists without downloading
     * Returns the file if it exists, null otherwise
     */
    private fun checkModelFileExists(): File? {
        val modelDir = File(getExternalFilesDir(null), "models/llama_cpp")
        if (!modelDir.exists()) {
            modelDir.mkdirs()
            Log.d("MyApp", "📂 Model directory created")
            return null
        }

        val modelFile = File(modelDir, "smollm2-360m-q8_0.gguf")

        // Check if file exists and is reasonable size (at least 100MB for this model)
        if (modelFile.exists() && modelFile.length() > 100_000_000) {
            return modelFile
        }

        // Check for Qwen model too
        val qwenFile = File(modelDir, "qwen2.5-0.5b-instruct-q6_k.gguf")
        if (qwenFile.exists() && qwenFile.length() > 100_000_000) {
            return qwenFile
        }

        return null
    }

    /**
     * Check if there's enough disk space for download
     */
    private fun hasEnoughDiskSpace(requiredMB: Long): Boolean {
        val externalDir = getExternalFilesDir(null) ?: return false
        val freeSpace = externalDir.freeSpace / (1024 * 1024)
        Log.d("MyApp", "💾 Free disk space: ${freeSpace}MB, Required: ${requiredMB}MB")
        return freeSpace > requiredMB + 100 // Extra 100MB buffer
    }

    /**
     * Load model with robust multi-strategy approach with proper timeout and validation
     * Called from UI when user manually loads a model
     */
    suspend fun loadModelRobust(modelName: String, modelFilePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("MyApp", "🚀 Starting robust model load for: $modelName")

                // Check memory before loading
                if (!canHandleModelLoading(this@MyApplication)) {
                    Log.e("MyApp", "❌ Insufficient resources for model loading")
                    return@withContext false
                }

                // CRITICAL: Always scan before loading with timeout
                Log.d("MyApp", "🔄 Scanning for downloaded models...")
                try {
                    withTimeout(5000) {
                        RunAnywhere.scanForDownloadedModels()
                    }
                    Log.i("MyApp", "✅ Scan complete")
                } catch (e: Exception) {
                    Log.w("MyApp", "⚠️ Scan timeout: ${e.message}")
                }

                var loaded = false

                // Strategy 1: Try loading by name (NO TIMEOUT - can take several minutes)
                if (!loaded) {
                    try {
                        Log.d("MyApp", "🔍 Strategy 1: Attempting load by name: $modelName")
                        loaded = RunAnywhere.loadModel(modelName)
                        if (loaded) {
                            Log.i("MyApp", "✅ Model successfully loaded by name!")
                            _modelReady.value = true
                            return@withContext true
                        }
                    } catch (e: Exception) {
                        Log.w("MyApp", "⚠️ Load by name failed: ${e.message}")
                    }
                }

                // Strategy 2: Try loading by ID (search through available models) (NO TIMEOUT)
                if (!loaded) {
                    try {
                        Log.d("MyApp", "🆔 Strategy 2: Searching for model in registry...")
                        val models = withTimeout(5000) {
                            listAvailableModels()
                        }
                        val modelInfo = models.find { it.name == modelName }

                        if (modelInfo != null) {
                            Log.d("MyApp", "🆔 Found model with ID: ${modelInfo.id}")
                            loaded = RunAnywhere.loadModel(modelInfo.id)
                            if (loaded) {
                                Log.i("MyApp", "✅ Model successfully loaded by ID!")
                                _modelReady.value = true
                                return@withContext true
                            }
                        } else {
                            Log.w("MyApp", "⚠️ Model not found in registry")
                        }
                    } catch (e: Exception) {
                        Log.w("MyApp", "⚠️ Load by ID failed: ${e.message}")
                    }
                }

                // Strategy 3: Try loading by file path with validation (NO TIMEOUT)
                if (!loaded && modelFilePath.isNotEmpty()) {
                    try {
                        Log.d("MyApp", "📂 Strategy 3: Attempting load by file path: $modelFilePath")

                        // Validate file exists
                        val file = File(modelFilePath)
                        if (!file.exists()) {
                            Log.w("MyApp", "⚠️ File does not exist: $modelFilePath")
                        } else if (file.length() < 1_000_000) {
                            Log.w("MyApp", "⚠️ File too small (corrupted?): ${file.length()} bytes")
                        } else {
                            Log.d("MyApp", "📂 File validated: ${file.length() / 1024 / 1024}MB")
                            loaded = RunAnywhere.loadModel(modelFilePath)
                            if (loaded) {
                                Log.i("MyApp", "✅ Model successfully loaded by file path!")
                                _modelReady.value = true
                                return@withContext true
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("MyApp", "⚠️ Load by file path failed: ${e.message}")
                    }
                }

                // All strategies failed
                Log.e("MyApp", "❌ All loading strategies failed")
                _modelReady.value = false
                return@withContext false

            } catch (e: Exception) {
                Log.e("MyApp", "❌ Model loading error: ${e.message}", e)
                _modelReady.value = false
                return@withContext false
            }
        }
    }

    /**
     * Ensures model file exists locally
     * Downloads if missing - this can take time but doesn't block UI
     */
    private suspend fun ensureSmolModelPresent(): File? {
        val modelDir = File(getExternalFilesDir(null), "models/llama_cpp")
        if (!modelDir.exists()) {
            val created = modelDir.mkdirs()
            Log.d("MyApp", "📂 Model directory created: $created")
        }

        val modelFile = File(modelDir, "smollm2-360m-q8_0.gguf")

        // If file exists and is reasonable size, assume it's good
        if (modelFile.exists() && modelFile.length() > 100_000_000) {
            Log.i(
                "MyApp",
                "✅ Model file exists: ${modelFile.absolutePath} (${modelFile.length() / 1024 / 1024} MB)"
            )
            return modelFile
        }

        // If file exists but is too small, delete it (corrupted download)
        if (modelFile.exists()) {
            Log.w("MyApp", "⚠️ Model file too small, deleting: ${modelFile.length()} bytes")
            modelFile.delete()
        }

        // Check disk space before downloading
        if (!hasEnoughDiskSpace(400)) { // SmolLM2 is ~360MB
            Log.e("MyApp", "❌ Insufficient disk space for download")
            return null
        }

        // Download model
        return downloadModelFile(modelFile)
    }

    /**
     * Download model file with progress logging and error recovery
     * Runs entirely in background - doesn't block UI
     */
    private suspend fun downloadModelFile(outputFile: File): File? {
        val url =
            "https://huggingface.co/Triangle104/SmolLM2-360M-Q8_0-GGUF/resolve/main/smollm2-360m-q8_0.gguf"

        Log.i("MyApp", "⬇️ Starting model download to: ${outputFile.absolutePath}")

        return withContext(Dispatchers.IO) {
            try {
                // Use timeout for connection
                val conn = withTimeout(30000) {
                    URL(url).openConnection()
                }
                conn.connectTimeout = 30_000 // 30 seconds
                conn.readTimeout = 30_000

                val totalSize = conn.contentLengthLong.takeIf { it > 0L } ?: -1L
                Log.d("MyApp", "📦 Download size: ${totalSize / 1024 / 1024} MB")

                conn.getInputStream().use { input ->
                    outputFile.outputStream().use { output ->
                        val buffer = ByteArray(256 * 1024) // 256KB buffer
                        var bytesRead: Int
                        var downloadedBytes = 0L
                        var lastLoggedPercent = -1

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead

                            // Log progress every 10%
                            if (totalSize > 0) {
                                val percent = (downloadedBytes * 100f / totalSize).roundToInt()
                                if (percent != lastLoggedPercent && percent % 10 == 0) {
                                    Log.d(
                                        "MyApp",
                                        "📦 Download progress: $percent% (${downloadedBytes / 1024 / 1024} MB)"
                                    )
                                    lastLoggedPercent = percent
                                }
                            }
                        }
                    }
                }

                val finalSize = outputFile.length()
                Log.i("MyApp", "✅ Download complete: ${finalSize / 1024 / 1024} MB")

                // Validate downloaded file
                if (finalSize < 100_000_000) {
                    Log.e("MyApp", "❌ Downloaded file too small, likely corrupted")
                    outputFile.delete()
                    return@withContext null
                }

                // Notify SDK about the new file
                try {
                    withTimeout(5000) {
                        RunAnywhere.scanForDownloadedModels()
                    }
                } catch (e: Exception) {
                    Log.w("MyApp", "⚠️ Post-download scan timeout: ${e.message}")
                }

                outputFile

            } catch (e: Exception) {
                Log.e("MyApp", "❌ Download failed: ${e.message}", e)

                // Clean up partial download
                if (outputFile.exists()) {
                    outputFile.delete()
                    Log.d("MyApp", "🗑️ Cleaned up partial download")
                }

                null
            }
        }
    }

    /**
     * Register model metadata with SDK
     * This is fast and just adds entries to the registry
     */
    private suspend fun registerModels() {
        try {
            // Register SmolLM2 360M
            addModelFromURL(
                url = "https://huggingface.co/Triangle104/SmolLM2-360M-Q8_0-GGUF/resolve/main/smollm2-360m-q8_0.gguf",
                name = "SmolLM2 360M Q8_0",
                type = "LLM"
            )
            Log.d("MyApp", "📦 Registered: SmolLM2 360M Q8_0")

            // Register Qwen 2.5 0.5B
            addModelFromURL(
                url = "https://huggingface.co/Triangle104/Qwen2.5-0.5B-Instruct-Q6_K-GGUF/resolve/main/qwen2.5-0.5b-instruct-q6_k.gguf",
                name = "Qwen 2.5 0.5B Instruct Q6_K",
                type = "LLM"
            )
            Log.d("MyApp", "📦 Registered: Qwen 2.5 0.5B Instruct Q6_K")

        } catch (e: Exception) {
            Log.e("MyApp", "⚠️ Model registration error: ${e.message}", e)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w("MyApp", "⚠️ LOW MEMORY WARNING - System under memory pressure")
        // Could unload model here if needed
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.w("MyApp", "⚠️ Memory trim requested: level=$level")
        // Handle memory pressure
    }
}
