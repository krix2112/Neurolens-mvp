package com.runanywhere.startup_hackathon20.services

import android.util.Log
import com.runanywhere.startup_hackathon20.ai.ModelsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EmotionService {
    private const val TAG = "EmotionService"

    /**
     * Detects emotion from text input using the loaded AI model (if available).
     * If no model is loaded, it skips inference and returns "neutral".
     */
    suspend fun detectEmotion(text: String): String = withContext(Dispatchers.IO) {
        try {
            val model = ModelsLoader.getLoadedModel()
            if (model == null) {
                Log.w(TAG, "No model loaded — skipping inference, returning neutral.")
                return@withContext "neutral"
            }

            // Prepare prompt for the model
            val prompt = """
                You are an emotion detector.
                Text: "$text"
                Respond ONLY with one word: happy, sad, angry, stressed, calm, or neutral.
            """.trimIndent()

            // Try to find an inference method (SDKs differ slightly)
            val result = try {
                val inferMethod = model.javaClass.methods.firstOrNull {
                    it.name.contains("infer", ignoreCase = true)
                }
                inferMethod?.invoke(model, prompt)?.toString() ?: "neutral"
            } catch (e: Exception) {
                Log.e(TAG, "Inference skipped: ${e.message}")
                "neutral"
            }

            // Normalize emotion output
            val emotion = result.lowercase()
                .replace("[^a-z]".toRegex(), "")
                .let {
                    when {
                        it.contains("stress") -> "stressed"
                        it.contains("sad") -> "sad"
                        it.contains("angry") -> "angry"
                        it.contains("calm") -> "calm"
                        it.contains("happy") -> "happy"
                        else -> "neutral"
                    }
                }

            Log.d(TAG, "Detected emotion: $emotion")
            return@withContext emotion

        } catch (e: Exception) {
            Log.e(TAG, "detectEmotion error: ${e.message}")
            "neutral"
        }
    }
}
