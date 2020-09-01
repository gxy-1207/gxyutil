package com.core.ui.activity.web

import android.os.Message
import com.orhanobut.logger.Logger
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

class CustomWebChromeClient(private val handler: WebHandler) : WebChromeClient() {

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        if (null == consoleMessage) {
            return super.onConsoleMessage(consoleMessage)
        }
        val level = consoleMessage.messageLevel() ?: ConsoleMessage.MessageLevel.ERROR
        val lineNumber = consoleMessage.lineNumber()
        val sourceId = consoleMessage.sourceId()
        val message = consoleMessage.message()
        val msg = "($lineNumber)\t$sourceId\t$message"
        when (level) {
            ConsoleMessage.MessageLevel.TIP,
            ConsoleMessage.MessageLevel.LOG -> Logger.i(msg)
            ConsoleMessage.MessageLevel.WARNING -> Logger.w(msg)
            ConsoleMessage.MessageLevel.ERROR -> Logger.e(msg)
            ConsoleMessage.MessageLevel.DEBUG -> Logger.d(msg)
        }
        return true
    }

    override fun onProgressChanged(view: WebView?, progress: Int) {
        super.onProgressChanged(view, progress)
        //显示进度
        val message = Message.obtain()
        message.what = MSG_PROGRESS_CHANGED
        message.arg1 = progress
        handler.sendMessage(message)
    }

}