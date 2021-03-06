package com.gxy.utils.test

import android.content.Context
import android.content.Intent
import com.core.ui.activity.web.BaseWebActivity
import com.gxy.utils.R
import com.gxy.utils.base.annotation.InjectLayout

@InjectLayout(toolbarLayoutId = R.layout.layout_toolbar_center_title)
class TestWebActivity : BaseWebActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TestWebActivity::class.java)
//                .putExtra()
            context.startActivity(starter)
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        setStatusBarColor(resources.getColor(R.color.text_red))
        changeStatusBarTextColor(false)
        setToolbarBackgroundColor(resources.getColor(R.color.text_red))
        setToolbarText(R.id.tv_global_title, "网页")
        setToolbarTextColor(R.id.tv_global_title, resources.getColor(R.color.text_white))
        setToolbarLeftImage(R.mipmap.go_balk) { onBackPressed() }
    }

    override fun provideLoadUrl(): String {
        return "https://www.baidu.com"
    }
}