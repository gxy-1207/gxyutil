package com.core.ui.activity.web

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.gxy.utils.R
import com.gxy.utils.base.act.BaseEmptyActivity
import com.gxy.utils.utils.LoggerUtil
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.activity_base_webview.*

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/31
 * @Description:     webview的基类
 */
abstract class BaseWebActivity : BaseEmptyActivity() {

    private lateinit var handler: WebHandler

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        handler = WebHandler(this)
    }

    override fun onSetViewData() {
        super.onSetViewData()
        //替换layoutView布局
        layoutView = inflateView(R.layout.activity_base_webview)
        getContentView().addView(layoutView)

        pb_web_progress?.max = 100
        pb_web_progress?.progress = 0
        pb_web_progress?.visibility = View.VISIBLE

        window.setFormat(PixelFormat.TRANSLUCENT)
        @Suppress("DEPRECATION")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val ws = wv_web_content?.settings
        ws?.allowFileAccess = true
        ws?.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        ws?.setSupportZoom(false)
        ws?.builtInZoomControls = false
        ws?.useWideViewPort = true
        ws?.setSupportMultipleWindows(false)
        ws?.loadWithOverviewMode = true
        ws?.setAppCacheEnabled(true)
        ws?.databaseEnabled = true
        ws?.domStorageEnabled = true
        @Suppress("DEPRECATION")
        ws?.javaScriptEnabled = true
        ws?.setGeolocationEnabled(true)
        ws?.setAppCacheMaxSize(Long.MAX_VALUE)
        ws?.setAppCachePath(this.getDir("cache", 0).path)
        @Suppress("DEPRECATION")
        ws?.databasePath = this.getDir("databases", 0).path
        ws?.setGeolocationDatabasePath(this.getDir("geolocation", 0).path)
        // webSetting.setPageCacheCapacity(IX5ws.DEFAULT_CACHE_CAPACITY);
        @Suppress("DEPRECATION")
        ws?.pluginState = WebSettings.PluginState.ON_DEMAND
        ws?.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //ws?.setPreFectch(true)

        wv_web_content?.webChromeClient = CustomWebChromeClient(handler)
        wv_web_content?.webViewClient = CustomWebViewClient(handler)

        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().sync()
        val url = provideLoadUrl()
        LoggerUtil.Log("WebView load url is : $url")
        wv_web_content?.loadUrl(url)
        val extension = wv_web_content?.x5WebViewExtension
        LoggerUtil.Log("是否加载腾讯X5内核 ： ${extension != null}")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != wv_web_content) {
            wv_web_content.destroy()
        }
    }

    override fun onBackPressed() {
        if (wv_web_content.canGoBack()) {
            if (wv_web_content.url == provideLoadUrl()) {
                super.onBackPressed()
            } else {
                wv_web_content.goBack()
            }
        } else {
            super.onBackPressed()
        }
    }
    //===Desc:================================================================================

    protected abstract fun provideLoadUrl(): String


    //===Desc:================================================================================
    fun onProgressChanged(progress: Int) {
        if (progress >= 100) {
            pb_web_progress?.visibility = View.GONE
        } else {
            pb_web_progress?.max = 100
            pb_web_progress?.progress = progress
            pb_web_progress?.visibility = View.VISIBLE
        }
    }

    fun onStartLoad() {
        pb_web_progress?.visibility = View.VISIBLE
    }

    fun onLoadFinished() {
        pb_web_progress?.visibility = View.GONE
    }

}