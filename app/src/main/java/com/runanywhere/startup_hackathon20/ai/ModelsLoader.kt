package com.runanywhere.startup_hackathon20.ai

import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere

object ModelsLoader {
    private const val TAG = "ModelsLoader"

    fun getLoadedModel(): Any? {
        return try {
            // Try the most common method first
            val method = RunAnywhere::class.java.methods.firstOrNull {
                it.name.contains("getLoaded", ignoreCase = true)
            }

            val result = method?.invoke(RunAnywhere)
            val modelsList = when (result) {
                is List<*> -> result
                else -> null
            }

            if (!modelsList.isNullOrEmpty()) {
                val model = modelsList.first()
                Log.d(TAG, "Loaded model found dynamically: $model")
                model
            } else {
                Log.w(TAG, "No models loaded yet.")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error accessing loaded model: ${e.message}")
            null
        }
    }
}
