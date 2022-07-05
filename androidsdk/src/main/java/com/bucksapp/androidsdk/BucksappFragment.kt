package com.bucksapp.androidsdk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * A simple [Fragment] subclass.
 * Use the [BucksappFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BucksappFragment : Fragment() {

    private var uuid: String? = null
    private var apiKey: String? = null
    private var env: String? = null
    private var language: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            apiKey = requireArguments().getString(BucksappFragment.ARG_API_KEY)
            uuid = requireArguments().getString(BucksappFragment.ARG_UUID)
            env = requireArguments().getString(BucksappFragment.ARG_ENV)
            language = requireArguments().getString(BucksappFragment.ARG_LANGUAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bucksapp, container, false)
        val webView = root.findViewById<WebView>(R.id.webView)
        val webViewClient = WebViewClient()
        webView.webViewClient = webViewClient
        val signatures = SignatureUtil.getSignatures(
            requireContext().packageManager,
            requireContext().applicationInfo.packageName
        )
        var signature = ""
        if (signatures != null) {
            if (signatures.isNotEmpty()) {
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
        val host: String = Bucksapp.getHost(env)
        val request: Request = Request.Builder()
            .url("$host/api/authenticate")
            .method("POST", body)
            .addHeader("jwt_aud", env!!)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-KEY", apiKey!!)
            .addHeader("package_name", requireContext().applicationInfo.packageName)
            .addHeader("build_signature", signature)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @kotlin.Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    response.body.use { responseBody ->
                        if (!response.isSuccessful) throw IOException(
                            """Unexpected code  ${response.body!!.string()}
 Headers: ${request.headers}"""
                        )
                        val authResponse: Bucksapp.AuthResponse =
                            gson.fromJson<Bucksapp.AuthResponse>(
                                responseBody!!.string(),
                                Bucksapp.AuthResponse::class.java
                            )
                        token = authResponse.token
                        webView.post {
                            val cookieManager = CookieManager.getInstance()
                            cookieManager.setAcceptCookie(true)
                            cookieManager.removeAllCookie()
                            cookieManager.setCookie(host, String.format("token=%s;", token))
                            cookieManager.setCookie(
                                host,
                                String.format("NEXT_LOCALE=%s;", language)
                            )
                            webView.loadUrl(host)
                            webView.settings.javaScriptEnabled = true
                            webView.addJavascriptInterface(
                                WebAppInterface(context!!),
                                "BucksappAndroid"
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        // Inflate the layout for this fragment
        return root
    }

    companion object {
        private const val ARG_UUID = "uuid"
        private const val ARG_API_KEY = "apiKey"
        private const val ARG_LANGUAGE = "language"
        private const val ARG_ENV = "env"
        private val client = OkHttpClient()
        private const val defaultLanguage = "es"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param apiKey   API_KEY provided by Bucksapp.
         * @param uuid     User uuid.
         * @param env Language ['development', 'staging', 'sandbox',  'production'].
         * @param language Language ['es', 'en'].
         * @return A new instance of fragment BucksappFragment.
         */
        fun newInstance(
            apiKey: String?,
            uuid: String?,
            env: String?,
            language: String?
        ): BucksappFragment {
            val fragment = BucksappFragment()
            val args = Bundle()
            args.putString(ARG_API_KEY, apiKey)
            args.putString(ARG_UUID, uuid)
            args.putString(ARG_ENV, env)
            args.putString(ARG_LANGUAGE, language)
            fragment.arguments = args
            return fragment
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param apiKey   API_KEY provided by Bucksapp.
         * @param uuid     User uuid.
         * @param env Language ['development', 'staging', 'production'].
         * @return A new instance of fragment BucksappFragment.
         */
        fun newInstance(apiKey: String?, uuid: String?, env: String?): BucksappFragment {
            val fragment = BucksappFragment()
            val args = Bundle()
            args.putString(ARG_API_KEY, apiKey)
            args.putString(ARG_UUID, uuid)
            args.putString(ARG_ENV, env)
            args.putString(ARG_LANGUAGE, defaultLanguage)
            fragment.arguments = args
            return fragment
        }
    }
}