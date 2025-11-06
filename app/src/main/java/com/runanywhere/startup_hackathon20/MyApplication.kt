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

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize SDK asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            initializeSDK()
        }
    }

    private suspend fun initializeSDK() {
        try {
            Log.i("MyApp", "⚙️ Initializing RunAnywhere SDK...")

            // Step 1: Initialize SDK
            RunAnywhere.initialize(
                context = this@MyApplication,
                apiKey = "dev",  // Any string works in dev mode
                environment = SDKEnvironment.DEVELOPMENT
            )

            // Step 2: Register LLM Service Provider
            LlamaCppServiceProvider.register()
            Log.i("MyApp", "✅ LlamaCpp service provider registered")

            // Step 3: Register models
            registerModels()
            Log.i("MyApp", "✅ Model registration complete")

            // Step 4: Scan for previously downloaded models
            RunAnywhere.scanForDownloadedModels()
            Log.i("MyApp", "🔍 Scanned for downloaded models")
            try {
                val success = RunAnywhere.loadModel("SmolLM2 360M Q8_0")
                if (success) {
                    Log.i("MyApp", "🚀 Successfully loaded SmolLM2 model directly in MyApplication.kt")
                } else {
                    Log.w("MyApp", "⚠️ Model load returned false — likely memory limit on emulator")
                }
            } catch (e: Exception) {
                Log.e("MyApp", "❌ Direct model load test failed: ${e.message}")
            }


            // Step 5: Sanity test – list models
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
            // ⚡ Small model – should load on most emulators and phones
            addModelFromURL(
                url = "https://huggingface.co/Triangle104/SmolLM2-360M-Q8_0-GGUF/resolve/main/smollm2-360m-q8_0.gguf",
                name = "SmolLM2 360M Q8_0",
                type = "LLM"
            )
            Log.i("MyApp", "📦 Registered SmolLM2 360M Q8_0")

            // 🧠 Larger model (optional — may fail on emulators)
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
