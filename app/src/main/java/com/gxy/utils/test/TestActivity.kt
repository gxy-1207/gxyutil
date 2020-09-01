package com.gxy.utils.test

import android.content.Context
import android.content.Intent
import com.gxy.utils.R
import com.gxy.utils.base.act.BaseEmptyActivity
import com.gxy.utils.base.annotation.InjectLayout
import com.gxy.utils.base.annotation.InjectPresenter
import kotlinx.android.synthetic.main.activity_test.*

@InjectLayout(
    layoutId = R.layout.activity_test,
    lodingLayoutId = R.layout.activity_test_loding,
    emptyLayoutId = R.layout.layout_empty,
    errorLayoutId = R.layout.layout_error,
    toolbarLayoutId = R.layout.layout_toolbar_center_title
)
class TestActivity : BaseEmptyActivity(), TestView {
    @InjectPresenter
    private lateinit var testPresenter: TestPresenter

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TestActivity::class.java)
//                .putExtra()
            context.startActivity(starter)
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        setStatusBarColor(resources.getColor(R.color.text_red))
        changeStatusBarTextColor(false)
        setToolbarBackgroundColor(resources.getColor(R.color.text_red))
        setToolbarText(R.id.tv_global_title, "基本")
        setToolbarTextColor(R.id.tv_global_title, resources.getColor(R.color.text_white))
        setToolbarLeftImage(R.mipmap.go_balk) { onBackPressed() }
        //http://ceshi.yuntaifawu.com/api/adviser/get_adviser_content
        //uid=1011&id=97
        testPresenter.getTestActivity(1011.toString(), 97.toString())
    }

    override fun onTestSuccess(testBean: TestBean) {
        tv_test_activity_content.text = testBean.find.descript
    }

    override fun onTestError(error: String) {

    }

}