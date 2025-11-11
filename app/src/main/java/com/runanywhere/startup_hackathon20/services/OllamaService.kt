package com.runanywhere.startup_hackathon20.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import org.json.JSONArray
import java.util.concurrent.TimeUnit

/**
 * Ollama Service - Integrates with Ollama server for AI model inference
 * Supports both local and remote Ollama instances
 */
object OllamaService {
    private const val TAG = "OllamaService"
    
    // Default to localhost, can be changed to remote server
    var baseUrl = "http://10.0.2.2:11434" // Android emulator localhost
    var currentModel = "llama2" // Default model
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Configure Ollama connection
     */
    fun configure(serverUrl: String, modelName: String = "llama2") {
        baseUrl = serverUrl.trimEnd('/')
        currentModel = modelName
        Log.i(TAG, "🔧 Configured: $baseUrl, model: $modelName")
    }
    
    /**
     * Test connection to Ollama server
     */
    suspend fun testConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/api/tags")
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            val isSuccess = response.isSuccessful
            
            if (isSuccess) {
                val body = response.body?.string()
                Log.i(TAG, "✅ Connected to Ollama server")
                Log.d(TAG, "Available models: $body")
            } else {
                Log.e(TAG, "❌ Connection failed: ${response.code}")
            }
            
            response.close()
            isSuccess
        } catch (e: Exception) {
            Log.e(TAG, "❌ Connection error: ${e.message}", e)
            false
        }
    }
    
    /**
     * List available models on Ollama server
     */
    suspend fun listModels(): List<OllamaModel> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/api/tags")
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e(TAG, "❌ Failed to list models: ${response.code}")
                return@withContext emptyList()
            }
            
            val body = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(body)
            val modelsArray = json.optJSONArray("models") ?: return@withContext emptyList()
            
            val models = mutableListOf<OllamaModel>()
            for (i in 0 until modelsArray.length()) {
                val modelJson = modelsArray.getJSONObject(i)
                models.add(
                    OllamaModel(
                        name = modelJson.getString("name"),
                        size = modelJson.optLong("size", 0),
                        modifiedAt = modelJson.optString("modified_at", "")
                    )
                )
            }
            
            Log.i(TAG, "📋 Found ${models.size} Ollama models")
            response.close()
            models
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error listing models: ${e.message}", e)
            emptyList()
        }
    }
    
    /**
     * Pull a model from Ollama library
     */
    suspend fun pullModel(modelName: String): Flow<PullProgress> = flow {
        withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("name", modelName)
                }
                
                val request = Request.Builder()
                    .url("$baseUrl/api/pull")
                    .post(json.toString().toRequestBody("application/json".toMediaType()))
                    .build()
                
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    emit(PullProgress(modelName, 0f, "Failed: ${response.code}", false))
                    response.close()
                    return@withContext
                }
                
                // Stream pull progress
                val body = response.body?.byteStream() ?: return@withContext
                body.bufferedReader().use { reader ->
                    reader.lineSequence().forEach { line ->
                        try {
                            val progressJson = JSONObject(line)
                            val status = progressJson.optString("status", "")
                            val total = progressJson.optLong("total", 0)
                            val completed = progressJson.optLong("completed", 0)
                            
                            val progress = if (total > 0) {
                                (completed.toFloat() / total.toFloat())
                            } else 0f
                            
                            emit(PullProgress(modelName, progress, status, false))
                            
                            if (status.contains("success", ignoreCase = true)) {
                                emit(PullProgress(modelName, 1f, "Complete", true))
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Error parsing pull progress: ${e.message}")
                        }
                    }
                }
                response.close()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Pull error: ${e.message}", e)
                emit(PullProgress(modelName, 0f, "Error: ${e.message}", false))
            }
        }
    }
    
    /**
     * Generate text using Ollama model (streaming)
     */
    suspend fun generateStream(prompt: String, model: String = currentModel): Flow<String> = flow {
        withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("model", model)
                    put("prompt", prompt)
                    put("stream", true)
                }
                
                val request = Request.Builder()
                    .url("$baseUrl/api/generate")
                    .post(json.toString().toRequestBody("application/json".toMediaType()))
                    .build()
                
                Log.i(TAG, "🤖 Generating with Ollama model: $model")
                
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.e(TAG, "❌ Generation failed: ${response.code}")
                    emit("Error: Failed to generate response (${response.code})")
                    response.close()
                    return@withContext
                }
                
                // Stream response tokens
                val body = response.body?.byteStream() ?: return@withContext
                body.bufferedReader().use { reader ->
                    reader.lineSequence().forEach { line ->
                        try {
                            val responseJson = JSONObject(line)
                            val token = responseJson.optString("response", "")
                            if (token.isNotEmpty()) {
                                emit(token)
                            }
                            
                            val done = responseJson.optBoolean("done", false)
                            if (done) {
                                Log.i(TAG, "✅ Generation complete")
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Error parsing response: ${e.message}")
                        }
                    }
                }
                response.close()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Generation error: ${e.message}", e)
                emit("Error: ${e.message}")
            }
        }
    }
    
    /**
     * Generate text using Ollama model (non-streaming)
     */
    suspend fun generate(prompt: String, model: String = currentModel): String = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("model", model)
                put("prompt", prompt)
                put("stream", false)
            }
            
            val request = Request.Builder()
                .url("$baseUrl/api/generate")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            Log.i(TAG, "🤖 Generating with Ollama model: $model")
            
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e(TAG, "❌ Generation failed: ${response.code}")
                response.close()
                return@withContext "Error: Failed to generate response"
            }
            
            val body = response.body?.string() ?: return@withContext "Error: Empty response"
            val responseJson = JSONObject(body)
            val result = responseJson.optString("response", "")
            
            Log.i(TAG, "✅ Generation complete")
            response.close()
            result
        } catch (e: Exception) {
            Log.e(TAG, "❌ Generation error: ${e.message}", e)
            "Error: ${e.message}"
        }
    }
    
    /**
     * Chat with context using Ollama model (streaming)
     */
    suspend fun chatStream(
        messages: List<OllamaChatMessage>,
        model: String = currentModel
    ): Flow<String> = flow {
        withContext(Dispatchers.IO) {
            try {
                val messagesArray = JSONArray()
                messages.forEach { msg ->
                    messagesArray.put(JSONObject().apply {
                        put("role", if (msg.isUser) "user" else "assistant")
                        put("content", msg.text)
                    })
                }
                
                val json = JSONObject().apply {
                    put("model", model)
                    put("messages", messagesArray)
                    put("stream", true)
                }
                
                val request = Request.Builder()
                    .url("$baseUrl/api/chat")
                    .post(json.toString().toRequestBody("application/json".toMediaType()))
                    .build()
                
                Log.i(TAG, "💬 Chatting with Ollama model: $model")
                
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.e(TAG, "❌ Chat failed: ${response.code}")
                    emit("Error: Failed to chat (${response.code})")
                    response.close()
                    return@withContext
                }
                
                // Stream response tokens
                val body = response.body?.byteStream() ?: return@withContext
                body.bufferedReader().use { reader ->
                    reader.lineSequence().forEach { line ->
                        try {
                            val responseJson = JSONObject(line)
                            val messageJson = responseJson.optJSONObject("message")
                            val token = messageJson?.optString("content", "") ?: ""
                            if (token.isNotEmpty()) {
                                emit(token)
                            }
                            
                            val done = responseJson.optBoolean("done", false)
                            if (done) {
                                Log.i(TAG, "✅ Chat complete")
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Error parsing chat response: ${e.message}")
                        }
                    }
                }
                response.close()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Chat error: ${e.message}", e)
                emit("Error: ${e.message}")
            }
        }
    }
}

/**
 * Ollama model information
 */
data class OllamaModel(
    val name: String,
    val size: Long,
    val modifiedAt: String
)

/**
 * Model pull progress
 */
data class PullProgress(
    val modelName: String,
    val progress: Float, // 0.0 to 1.0
    val status: String,
    val complete: Boolean
)

/**
 * Chat message for Ollama chat API (renamed to avoid conflict with main ChatMessage)
 */
data class OllamaChatMessage(
    val text: String,
    val isUser: Boolean
)
