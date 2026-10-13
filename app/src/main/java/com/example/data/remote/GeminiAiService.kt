package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAiService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Empathetic AI Vent Assistant:
     * Listens without judgment, validates emotions, avoids unsolicited orders,
     * reflects understanding, and offers gentle comfort.
     */
    suspend fun generateVentReflection(
        userVentTitle: String,
        userVentContent: String,
        mood: String,
        isBengali: Boolean
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        val systemPrompt = if (isBengali) {
            "তুমি 'মন খুলে' (MONKHULE) অ্যাপের একটি সহানুভূতিশীল ও মমতাময়ী AI সহচর। " +
            "ব্যবহারকারী তার মনের দুঃখ, ক্ষোভ বা হতাশা শেয়ার করেছেন। " +
            "তুমি কখনোই বিচার করবে না, কড়া উপদেশ দেবে না, বা তুচ্ছ করবে না। " +
            "তার অনুভূতিকে স্বীকৃতি দাও, বলো যে সে একা নয়, এবং খুব শান্ত ও উষ্ণ ভাষায় ৩-৪ বাক্যে সহমর্মিতা প্রকাশ করো। " +
            "মনে করিয়ে দাও যে তুমি একজন সহানুভূতিশীল শ্রোতা মাত্র, কোনো লাইসেন্সপ্রাপ্ত থেরাপিস্ট নও।"
        } else {
            "You are MONKHULE's Empathetic AI Vent Assistant. " +
            "The user is venting raw emotions, frustration, or sadness. " +
            "Do NOT judge, do NOT dismiss their pain, and do NOT force generic unsolicited advice. " +
            "Validate their emotions deeply, acknowledge their struggle, and offer warm emotional presence in 3-4 gentle sentences. " +
            "Clarify gently that you are an empathetic companion, not a licensed therapist."
        }

        val userPrompt = "Mood: $mood\nTitle: $userVentTitle\nVent Content: $userVentContent"

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Intelligent empathetic fallback when API key is not yet set in AI Studio Secrets
            return@withContext if (isBengali) {
                "🤖 [AI সহচর]: আপনার ভেতরের কষ্ট ও ক্লান্তি আমি গভীরভাবে অনুভব করছি। এত চাপ ও অনুভূতির ভার একা বহন করা সত্যিই কঠিন। আপনার মনের এই ক্ষোভ সম্পূর্ণ স্বাভাবিক। মনে রাখবেন, মন খুলে প্রকাশ করতে পারাটাই সাহসের লক্ষণ। আপনি একা নন, আমরা আপনার পাশে আছি। (দয়া করে নিজেকে আজ রাতে একটু শান্তি দিন।)"
            } else {
                "🤖 [AI Vent Assistant]: I hear the weight and exhaustion in your words. Carrying this emotional burden alone takes a massive toll, and your feelings are completely valid. Reaching out and putting this into words is an act of strength. You do not have to carry everything by yourself—take a gentle breath, and remember you are heard."
            }
        }

        try {
            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().put("text", "$systemPrompt\n\n$userPrompt"))
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val resJson = JSONObject(responseString)
                val candidates = resJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        return@withContext "🤖 [AI সহচর]: $text"
                    }
                }
            }
            // Fallback on error response
            if (isBengali) {
                "🤖 [AI সহচর]: আপনার অনুভূতিগুলো মূল্যবান। আমরা আপনার পাশে আছি। মন খুলে বলার জন্য ধন্যবাদ।"
            } else {
                "🤖 [AI Vent Assistant]: Your feelings are valid and you are heard. Thank you for trusting this space to express yourself."
            }
        } catch (e: Exception) {
            if (isBengali) {
                "🤖 [AI সহচর]: আপনার অনুভূতিগুলো সম্পূর্ণ স্বাভাবিক। এত চাপ নেওয়া সহজ নয়। আমরা আপনার পাশে আছি।"
            } else {
                "🤖 [AI Vent Assistant]: Your feelings are completely valid. It is okay to be tired and seek relief. We hear you."
            }
        }
    }

    /**
     * AI Writing Helper:
     * Cleans up messy raw thoughts into well-structured, clear text while preserving original emotion.
     */
    suspend fun assistWriting(draftText: String, isBengali: Boolean): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide clean formatting
            return@withContext draftText.trim()
        }

        val prompt = if (isBengali) {
            "নিচের লেখাটির মূল ক্ষোভ বা আবেগ অক্ষুণ্ণ রেখে বাক্যগুলো সুন্দর ও স্পষ্ট করে সাজিয়ে দাও:\n$draftText"
        } else {
            "Clean up and organize the following user draft to be clear and articulate while strictly preserving its authentic raw emotion:\n$draftText"
        }

        try {
            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val resJson = JSONObject(responseString)
                val text = resJson.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text")
                if (!text.isNullOrBlank()) {
                    return@withContext text.trim()
                }
            }
            draftText
        } catch (e: Exception) {
            draftText
        }
    }
}
