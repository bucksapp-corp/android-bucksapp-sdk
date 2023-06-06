package com.bucksapp.androidsdk

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

/** Instantiate the interface and set the context  */
class WebAppInterface(private val mContext: Context) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun handlerBack() {
        val activity = mContext as BucksappActivity
        activity.finish()
    }
}

class BucksappActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bucksapp)
        var token: String? = null
        var lang: String? = null
        var host: String? = null
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras != null) {
                token = extras.getString("TOKEN")
                lang = extras.getString("LANG")
                host = extras.getString("HOST")
            }
        } else {
            token = savedInstanceState.getSerializable("TOKEN") as String?
            lang = savedInstanceState.getSerializable("LANG") as String?
            host = savedInstanceState.getSerializable("HOST") as String?
        }
        val webView = findViewById<WebView>(R.id.webView)

        val webViewClient = WebViewClient()
        webView.webViewClient = webViewClient
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.removeAllCookie()
        cookieManager.setCookie(host, String.format("token=%s;", token))
        cookieManager.setCookie(
            host,
            String.format("NEXT_LOCALE=%s;", lang)
        )
        if (host != null) {
            webView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    Log.d("ALERT","ESTO ES UNA ALERTA")
                    return super.onJsAlert(view, url, message, result)
                }

                override fun onJsConfirm(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    Log.d("CONFIRM","ESTO ES UNA CONFIRMACION")
                    return super.onJsConfirm(view, url, message, result)
                }
            }
            webView.loadUrl(host)
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(WebAppInterface(this), "BucksappAndroid")

        }

    }
}