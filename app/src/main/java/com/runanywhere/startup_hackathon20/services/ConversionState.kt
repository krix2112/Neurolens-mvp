package com.runanywhere.startup_hackathon20.services

data class ConversationState(
    val emotion: String = "neutral",
    val advice: List<String> = emptyList(),
    val lastMessage: String = "",
    val modelLoaded: Boolean = false,
    val isMockMode: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
