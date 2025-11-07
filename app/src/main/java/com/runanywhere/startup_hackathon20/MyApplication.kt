package com.runanywhere.startup_hackathon20

import android.app.Application
import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.data.models.SDKEnvironment
import com.runanywhere.sdk.public.extensions.addModelFromURL
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.llm.llamacpp.LlamaCppServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch(Dispatchers.IO) {
            initializeSDK()
        }
    }

    private suspend fun initializeSDK() {
        try {
            Log.i("MyApp", "⚙️ Initializing RunAnywhere SDK...")

            RunAnywhere.initialize(
                context = this@MyApplication,
                apiKey = "dev",
                environment = SDKEnvironment.DEVELOPMENT
            )

            LlamaCppServiceProvider.register()
            Log.i("MyApp", "✅ LlamaCpp service provider registered")

            registerModels()

            RunAnywhere.scanForDownloadedModels()
            Log.i("MyApp", "🔍 Scanned for downloaded models")

            val modelDir = File(getExternalFilesDir(null), "models/llama_cpp")
            Log.i("MyApp", "📂 Expected model directory: ${modelDir.absolutePath}")
            if (!modelDir.exists()) modelDir.mkdirs()

            val modelFile = File(modelDir, "smollm2-360m-q8_0.gguf")

            if (!modelFile.exists()) {
                Log.w("MyApp", "⚠️ Model file missing — forcing re-download...")
                val modelUrl = "https://huggingface.co/Triangle104/SmolLM2-360M-Q8_0-GGUF/resolve/main/smollm2-360m-q8_0.gguf"

                try {
                    modelFile.outputStream().use { out ->
                        java.net.URL(modelUrl).openStream().use { input ->
                            input.copyTo(out)
                        }
                    }
                    Log.i("MyApp", "✅ Model manually downloaded to ${modelFile.absolutePath}")
                } catch (e: Exception) {
                    Log.e("MyApp", "❌ Manual download failed: ${e.message}")
                }
            } else {
                Log.i("MyApp", "✅ Model file already exists at: ${modelFile.absolutePath}")
            }

            try {
                val success = RunAnywhere.loadModel("SmolLM2 360M Q8_0")
                if (success)
                    Log.i("MyApp", "🚀 Successfully loaded SmolLM2 model directly in MyApplication.kt")
                else
                    Log.w("MyApp", "⚠️ Model load returned false — possible memory limit on emulator")
            } catch (e: Exception) {
                Log.e("MyApp", "❌ Direct model load failed: ${e.message}")
            }

            try {
                val models = listAvailableModels()
                Log.i("MyApp", "📦 Models available: ${models.map { it.name }}")
            } catch (e: Exception) {
                Log.e("MyApp", "⚠️ Model listing test failed: ${e.message}")
            }

            Log.i("MyApp", "🎯 SDK initialized successfully")

        } catch (e: Exception) {
            Log.e("MyApp", "❌ SDK initialization failed: ${e.message}")
        }
    }

    private suspend fun registerModels() {
        try {
            addModelFromURL(
                url = "https://huggingface.co/Triangle104/SmolLM2-360M-Q8_0-GGUF/resolve/main/smollm2-360m-q8_0.gguf",
                name = "SmolLM2 360M Q8_0",
                type = "LLM"
            )
            Log.i("MyApp", "📦 Registered SmolLM2 360M Q8_0")

            addModelFromURL(
                url = "https://huggingface.co/Triangle104/Qwen2.5-0.5B-Instruct-Q6_K-GGUF/resolve/main/qwen2.5-0.5b-instruct-q6_k.gguf",
                name = "Qwen 2.5 0.5B Instruct Q6_K",
                type = "LLM"
            )
            Log.i("MyApp", "📦 Registered Qwen 2.5 0.5B Instruct Q6_K")
        } catch (e: Exception) {
            Log.e("MyApp", "⚠️ Error registering models: ${e.message}")
        }
    }
}
