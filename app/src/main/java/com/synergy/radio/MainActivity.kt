package com.synergy.radio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            // TRÈS IMPORTANT : Permet de lancer l'audio sans interaction tactile directe bloquante en arrière-plan
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")

        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun startForegroundService() {
            val intent = Intent(this@MainActivity, RadioService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this@MainActivity.startForegroundService(intent)
            } else {
                this@MainActivity.startService(intent)
            }
        }

        @JavascriptInterface
        fun stopForegroundService() {
            val intent = Intent(this@MainActivity, RadioService::class.java)
            this@MainActivity.stopService(intent)
        }
    }

    // IMPORTANT : On NE surcharge PAS onStop() ou onPause() pour détruire 
    // ou mettre en pause le WebView, sinon la musique se coupe dès que tu quittes l'app.
}
