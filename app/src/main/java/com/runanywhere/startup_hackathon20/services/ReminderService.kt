package com.runanywhere.startup_hackathon20.services

import android.util.Log
import com.runanywhere.startup_hackathon20.ai.ModelsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ReminderService {
    private const val TAG = "ReminderService"

    /**
     * Breaks a task into 2–3 mini steps depending on emotion.
     * If no model is loaded, uses local fallback suggestions.
     */
    suspend fun adaptTask(task: String, mood: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val model = ModelsLoader.getLoadedModel()

            // --- Fallback if model isn't loaded ---
            if (model == null) {
                Log.w(TAG, "No model loaded — using fallback nudges.")
                return@withContext generateFallback(task, mood)
            }

            val prompt = """
                You are a helpful focus assistant.
                The user feels "$mood" and needs to complete "$task".
                Break it into 3 short, actionable steps.
                Respond only as:
                1. Step one
                2. Step two
                3. Step three
            """.trimIndent()

            val inferMethod = model.javaClass.methods.firstOrNull {
                it.name.contains("infer", ignoreCase = true)
            }

            val raw = inferMethod?.invoke(model, prompt)?.toString() ?: ""
            val steps = raw.split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() && it[0].isDigit() }
                .take(3)

            if (steps.isEmpty()) return@withContext generateFallback(task, mood)

            Log.d(TAG, "Generated steps: $steps")
            return@withContext steps

        } catch (e: Exception) {
            Log.e(TAG, "adaptTask error: ${e.message}")
            generateFallback(task, mood)
        }
    }

    // Local fallback generator
    private fun generateFallback(task: String, mood: String): List<String> {
        return when (mood) {
            "stressed" -> listOf(
                "Pause 1 min and breathe.",
                "Break \"$task\" into the smallest piece.",
                "Finish that piece, then re-evaluate."
            )
            "sad" -> listOf(
                "Put on music you like.",
                "Start \"$task\" for just 5 minutes.",
                "Reward yourself after finishing."
            )
            "angry" -> listOf(
                "Walk away from screen for 2 minutes.",
                "Return and write one clear sentence of what to do.",
                "Do only that step."
            )
            "happy" -> listOf(
                "Channel your energy into \"$task\".",
                "Set a 15 min timer and go full focus.",
                "Share progress with a friend."
            )
            "calm" -> listOf(
                "Plan \"$task\" logically.",
                "Execute one part smoothly.",
                "Log completion to stay consistent."
            )
            else -> listOf(
                "Write down what \"$task\" means to you.",
                "Start small.",
                "Track how you feel after."
            )
        }
    }
}
