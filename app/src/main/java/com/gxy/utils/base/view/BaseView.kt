package com.gxy.utils.base.view

import android.content.Context
import androidx.annotation.StringRes

interface BaseView {
    fun getBindContext(): Context
    fun showLoding()
    fun hideLoding()

    fun showSuccess()
    fun hideSuccess()

    fun showEmpty()
    fun hideEmpty()

    fun showError()
    fun hideError()

    fun showWait(cancelOutSite: Boolean = false)
    fun hideWait(runnable: Runnable? = null)

    fun showToast(text: String, isLong: Boolean = false)
    fun showToast(@StringRes textId: Int, isLong: Boolean = false)


    fun runMainThread(runnable: Runnable)

}