package com.gxy.utils.interceptors

import com.gxy.utils.utils.JsonUtil
import com.gxy.utils.utils.LoggerUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/28
 * @Description:     日志拦截打印返回的数据在控制台
 */
class HeaderInterceptors : HttpLoggingInterceptor.Logger {
    private val Mmessage = StringBuilder()
    override fun log(message: String) {
        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            Mmessage.delete(0, Mmessage.length)
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化

        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        val isJson = (message.startsWith("{") && message.endsWith("}")
                || message.startsWith("[") && message.endsWith("]"))
        var msg = message
        if (isJson) {
            msg = JsonUtil.formatJson(JsonUtil.decodeUnicode(message) ?: "") ?: ""
        }
        Mmessage.append(
            msg.trimIndent()
        )
        // 响应结束，打印整条日志
        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            LoggerUtil.Log(Mmessage)
        }
    }
}