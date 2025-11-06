package com.runanywhere.startup_hackathon20.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HookService {
    private const val TAG = "HookService"

    suspend fun processJournal(text: String): ConversationState = withContext(Dispatchers.IO) {
        try {
            val emotion = EmotionService.detectEmotion(text)
            val steps = ReminderService.adaptTask(text, emotion)
            Log.d(TAG, "Processed emotion=$emotion steps=$steps")

            ConversationState(
                emotion = emotion,
                advice = steps,
                lastMessage = text,
                modelLoaded = true,
                isMockMode = true  // until model loads
            )
        } catch (e: Exception) {
            Log.e(TAG, "processJournal error: ${e.message}")
            ConversationState(
                emotion = "neutral",
                advice = listOf("Try again later."),
                lastMessage = text,
                modelLoaded = false,
                isMockMode = true
            )
        }
    }
}
