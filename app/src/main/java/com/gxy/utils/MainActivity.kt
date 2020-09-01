package com.gxy.utils

import android.os.Bundle
import android.view.View
import com.gxy.utils.base.act.BaseEmptyActivity
import com.gxy.utils.base.annotation.InjectLayout
import com.gxy.utils.test.TestActivity
import com.gxy.utils.test.TestRecycleActivity
import com.gxy.utils.test.TestWebActivity
import kotlinx.android.synthetic.main.activity_main.*

@InjectLayout(
    layoutId = R.layout.activity_main,
    toolbarLayoutId = R.layout.layout_toolbar_center_title
)
class MainActivity : BaseEmptyActivity() {

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
    }

    override fun onSetViewData() {
        super.onSetViewData()
        setToolbarText(R.id.tv_global_title, "主页")
    }

    override fun onSetViewListener() {
        super.onSetViewListener()
        btn_activity.setOnClickListener(View.OnClickListener {
            TestActivity.start(this)
        })
        btn_recycleview_activity.setOnClickListener(View.OnClickListener {
            TestRecycleActivity.start(this)
        })
        btn_web_activity.setOnClickListener(View.OnClickListener {
            TestWebActivity.start(this)
        })
    }
}