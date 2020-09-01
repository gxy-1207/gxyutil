package com.gxy.utils.app

import android.app.Application
import com.gxy.utils.interceptors.HeaderInterceptors
import com.gxy.utils.network.HttpHelper
import com.gxy.utils.utils.LoggerUtil
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //日志拦截器
        val httpLoggingInterceptor = HttpLoggingInterceptor(HeaderInterceptors())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val list = mutableListOf<Interceptor>()
        list.add(httpLoggingInterceptor)
        HttpHelper.init("https://yuntaifawu.com/api/", list)
        //初始化日志dayin
        LoggerUtil.initLogger("TAG_GXY", true)
    }
}