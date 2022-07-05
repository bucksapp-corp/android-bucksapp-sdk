package com.bucksapp.androidsdk

import android.content.Context
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody


object Bucksapp {
    private val client = OkHttpClient()
    private const val defaultLanguage = "es"
    private const val developmentEnv = "development"
    private const val stagingEnv = "staging"
    private const val productionEnv = "production"
    private const val sandboxEnv = "sandbox"
    private const val defaultEnv = stagingEnv

    fun getHost(env: String? = defaultEnv): String {

        when (env) {
            productionEnv -> return "https://app.bucksapp.com"
            stagingEnv -> return "https://app.stg.bucksapp.com"
            sandboxEnv -> return "https://app.sbx.bucksapp.com"
        }
        return "https://app.dev.bucksapp.com"
    }

    fun init(
        context: Context,
        apiKey: String,
        uuid: String,
        env: String = defaultEnv,
        language: String = defaultLanguage
    ) {
        val signatures = SignatureUtil.getSignatures(context.packageManager,
            context.applicationInfo.packageName)
        var signature = ""
        if (signatures != null) {
            if (signatures.isNotEmpty()){
                signature = signatures[0]!!
            }
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val gson = Gson()
        val jsonString = String.format(
            "{\"user\": \"%s\"}",
            uuid,
        )
        val body: RequestBody = jsonString.toRequestBody(mediaType);
        val host: String = getHost(env)
        val request: Request = Request.Builder()
            .url("$host/api/authenticate")
            .method("POST", body)
            .addHeader("jwt_aud", env)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-KEY", apiKey)
            .addHeader("package_name", context.applicationInfo.packageName)
            .addHeader("build_signature", signature)
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
                                |Headers: ${request.headers}""".trimMargin()
                        )
                        val authResponse =
                            gson.fromJson(responseBody!!.string(), AuthResponse::class.java)

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