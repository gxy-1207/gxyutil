package com.core.ui.activity.web

import android.graphics.Bitmap
import android.text.TextUtils
import com.orhanobut.logger.Logger
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

private const val REG_URL =
        "^(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?$"

class CustomWebViewClient(private val handler: WebHandler) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (null == url || TextUtils.isEmpty(url)) {
            return false
        }
        Logger.i("Will be load url is : $url")
        if (url.matches(Regex(REG_URL))) {
            return super.shouldOverrideUrlLoading(view, url)
        }
        //判断是否是电话,email
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        handler.sendEmptyMessage(MSG_START_LOAD)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        handler.sendEmptyMessage(MSG_LOAD_FINISHED)
    }

    override fun onReceivedSslError(
            view: WebView?,
            sslErrorHandler: SslErrorHandler?,
            error: SslError?
    ) {
        sslErrorHandler?.proceed()
    }

}