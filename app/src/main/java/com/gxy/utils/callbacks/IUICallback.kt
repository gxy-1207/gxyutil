package com.gxy.utils.callbacks

import android.os.Bundle

interface IUICallback {
    fun onInitData(savedInstanceState: Bundle?)
    fun onSetViewListener()

    fun onSetViewData()

    /**加载失败或者数据空是点击屏幕重试*/
    fun onRetryLoading()
}