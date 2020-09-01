package com.gxy.utils.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object LoggerUtil {
    fun initLogger(loggerTag: String, debug: Boolean) {
        // Logger
        Logger.addLogAdapter(
            object : AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder().tag(loggerTag).build()
            ) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return debug
                }
            })
    }

    // ===Desc:=================================================================
    fun Log(msg: Any) {
        Logger.e(msg.toString())
    }

    // ===Desc:=================================================================
    fun Log(msg: String) {
        Logger.e(msg.toString())
    }
}