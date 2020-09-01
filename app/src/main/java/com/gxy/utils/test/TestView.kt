package com.gxy.utils.test

import com.gxy.utils.base.view.BaseView

interface TestView : BaseView {
    fun onTestSuccess(testBean: TestBean)
    fun onTestError(error: String)
}