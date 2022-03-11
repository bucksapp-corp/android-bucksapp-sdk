package com.bucksapp.androidsdk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

class BucksappActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bucksapp)
        var token: String? = null
        var lang: String? = null
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras != null) {
                token = extras.getString("TOKEN")
                lang = extras.getString("LANG")
            }
        } else {
            token = savedInstanceState.getSerializable("TOKEN") as String?
            lang = savedInstanceState.getSerializable("LANG") as String?
        }
        val webView = findViewById<WebView>(R.id.webView)
        val webViewClient = WebViewClient()
        webView.webViewClient = webViewClient
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.removeAllCookie()
        cookieManager.setCookie("https://app.dev.bucksapp.com/", String.format("token=%s;", token))
        cookieManager.setCookie(
            "https://app.dev.bucksapp.com/",
            String.format("NEXT_LOCALE=%s;", lang)
        )
        webView.loadUrl("https://app.dev.bucksapp.com")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
    }
}