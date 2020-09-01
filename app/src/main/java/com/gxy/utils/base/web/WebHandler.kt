package com.core.ui.activity.web

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

const val MSG_START_LOAD = 0
const val MSG_PROGRESS_CHANGED = 1
const val MSG_LOAD_FINISHED = 2


class WebHandler(webActivity: BaseWebActivity) : Handler(Looper.getMainLooper()) {
    private val wr = WeakReference<BaseWebActivity>(webActivity)

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val value = wr.get() ?: return
        when (msg.what) {
            MSG_START_LOAD -> value.onStartLoad()
            MSG_PROGRESS_CHANGED -> value.onProgressChanged(msg.arg1)
            MSG_LOAD_FINISHED -> value.onLoadFinished()
        }


    }


}