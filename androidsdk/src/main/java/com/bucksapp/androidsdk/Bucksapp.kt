package com.bucksapp.androidsdk

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object Bucksapp {
    private val client = OkHttpClient()
    private const val defaultLanguage = "es"
    fun init(context: Context, apiKey: String, uuid: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val gson = Gson()
        val jsonString = String.format("{\"user\": \"%s\"}", uuid)
        val body: RequestBody = jsonString.toRequestBody(mediaType);
        val request: Request = Request.Builder()
            .url(java.lang.String.format("%s/api/fi/v1/authenticate", BuildConfig.API_URL))
            .method("POST", body)
            .addHeader("jwt_aud", BuildConfig.ENV)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-KEY", apiKey)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    response.body.use { responseBody ->
                        if (!response.isSuccessful) throw IOException(
                            """Unexpected code ${response.body!!.string()}
 Headers: ${request.headers}"""
                        )
                        val authResponse =
                            gson.fromJson(responseBody!!.string(), AuthResponse::class.java)
                        val i = Intent(context, BucksappActivity::class.java)
                        i.putExtra("TOKEN", authResponse.token)
                        i.putExtra("LANG", defaultLanguage)
                        context.startActivity(i)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun init(context: Context, apiKey: String, uuid: String, language: String) {
        val lang = if (language != null || language !== "") language else defaultLanguage
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val gson = Gson()
        val jsonString = String.format("{\"user\": \"%s\"}", uuid)
        val body: RequestBody = jsonString.toRequestBody(mediaType);
        val request: Request = Request.Builder()
            .url(java.lang.String.format("%s/api/fi/v1/authenticate", BuildConfig.API_URL))
            .method("POST", body)
            .addHeader("jwt_aud", BuildConfig.ENV)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-KEY", apiKey)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    response.body.use { responseBody ->
                        if (!response.isSuccessful) throw IOException(
                            """Unexpected code ${response.body!!.string()}
 Headers: ${request.headers}"""
                        )
                        val authResponse =
                            gson.fromJson(responseBody!!.string(), AuthResponse::class.java)
                        val i = Intent(context, BucksappActivity::class.java)
                        i.putExtra("TOKEN", authResponse.token)
                        i.putExtra("LANG", lang)
                        context.startActivity(i)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    internal class AuthResponse {
        var token: String? = null
    }
}