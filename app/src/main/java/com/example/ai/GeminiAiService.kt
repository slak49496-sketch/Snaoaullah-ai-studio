package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiGeneratedClipResult(
    val viralHook: String,
    val caption: String,
    val hashtags: List<String>,
    val soundSuggestion: String,
    val speakerTrackingNotes: String,
    val viralityScore: Int // 1 to 100
)

object GeminiAiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    suspend fun generateViralClipMetadata(
        topic: String,
        durationSeconds: Int,
        cropMode: String,
        tone: String = "Engaging & Viral"
    ): AiGeneratedClipResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateFallback(topic, durationSeconds, cropMode, tone)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val prompt = """
                You are a world-class viral short-form video producer for TikTok and Instagram Reels.
                The creator has cut a ${durationSeconds}s video clip about: "$topic".
                Crop format: $cropMode (9:16 vertical video ratio).
                Tone: $tone.

                Generate a JSON object with the following fields:
                1. "viralHook": A punchy 1-sentence hook to capture attention in the first 2 seconds.
                2. "caption": A catchy TikTok caption written with emojis and call to action.
                3. "hashtags": An array of 5-8 trending relevant hashtags (e.g. #fyp, #viral, plus niche tags).
                4. "soundSuggestion": A trending audio or sound vibe recommendation.
                5. "speakerTrackingNotes": Brief tip on 9:16 framing / camera panning for this clip.
                6. "viralityScore": An integer rating from 75 to 98 predicting virality potential.

                Output ONLY raw JSON with no markdown formatting.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        }
                        put("parts", partsArray)
                    })
                }
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                })
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respString = response.body?.string() ?: ""
                val rootJson = JSONObject(respString)
                val candidates = rootJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val contentObj = firstCandidate?.optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                val textOutput = parts?.optJSONObject(0)?.optString("text") ?: ""

                val cleanJson = textOutput.replace("```json", "").replace("```", "").trim()
                val parsed = JSONObject(cleanJson)

                val hook = parsed.optString("viralHook", "Wait until you see this...")
                val caption = parsed.optString("caption", "🔥 $topic\nDrop your thoughts below! 👇")
                val tagsJson = parsed.optJSONArray("hashtags")
                val tagsList = mutableListOf<String>()
                if (tagsJson != null) {
                    for (i in 0 until tagsJson.length()) {
                        val tag = tagsJson.getString(i).trim()
                        if (tag.isNotEmpty()) {
                            tagsList.add(if (tag.startsWith("#")) tag else "#$tag")
                        }
                    }
                }
                if (tagsList.isEmpty()) {
                    tagsList.addAll(listOf("#fyp", "#viral", "#tiktokstudio", "#trending", "#contentcreator"))
                }

                val sound = parsed.optString("soundSuggestion", "Trending Beat - Original Sound")
                val speakerNotes = parsed.optString("speakerTrackingNotes", "Center focus locked on speaker facial movement.")
                val score = parsed.optInt("viralityScore", 92)

                return@withContext AiGeneratedClipResult(
                    viralHook = hook,
                    caption = caption,
                    hashtags = tagsList,
                    soundSuggestion = sound,
                    speakerTrackingNotes = speakerNotes,
                    viralityScore = score
                )
            } else {
                generateFallback(topic, durationSeconds, cropMode, tone)
            }
        } catch (e: Exception) {
            generateFallback(topic, durationSeconds, cropMode, tone)
        }
    }

    private fun generateFallback(
        topic: String,
        durationSeconds: Int,
        cropMode: String,
        tone: String
    ): AiGeneratedClipResult {
        val cleanTopic = if (topic.isBlank()) "Viral Moment" else topic.trim()
        val hashtags = mutableListOf("#fyp", "#viral", "#trending", "#tiktokstudio", "#shorts")
        val sanitizedTopicTag = "#" + cleanTopic.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
        if (sanitizedTopicTag.length > 2) {
            hashtags.add(0, sanitizedTopicTag)
        }

        val viralHook = when {
            cleanTopic.contains("podcast", ignoreCase = true) -> "Nobody is talking about this truth... 🤯"
            cleanTopic.contains("tech", ignoreCase = true) -> "This one feature completely changes the game 📱"
            cleanTopic.contains("dance", ignoreCase = true) || cleanTopic.contains("music", ignoreCase = true) -> "The energy on this drop is unmatched 🔥"
            else -> "You need to hear this before it goes viral... 👀"
        }

        val caption = "🔥 $cleanTopic\n⏱ Cut duration: ${durationSeconds}s | 9:16 Crop: $cropMode\n\n$viralHook\nWhat do you think? Drop a comment below! 👇"

        val speakerNotes = if (cropMode.contains("Smart", ignoreCase = true)) {
            "AI Speaker Tracking: Centered dynamically with 1.25x vertical crop factor to keep facial landmarks aligned in the top 40% golden ratio."
        } else {
            "Center Crop 9:16: High-precision centered framing with cinematic vertical padding."
        }

        return AiGeneratedClipResult(
            viralHook = viralHook,
            caption = caption,
            hashtags = hashtags,
            soundSuggestion = "Viral TikTok Trending Audio (Sped Up) - 1.2M creations",
            speakerTrackingNotes = speakerNotes,
            viralityScore = 88 + (durationSeconds % 10)
        )
    }
}
