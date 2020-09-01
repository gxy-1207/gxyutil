package com.gxy.utils.test

import android.os.SystemClock
import com.gxy.utils.R
import com.gxy.utils.apis.TestApi
import com.gxy.utils.base.presenter.BaseRefreshPresenter
import com.gxy.utils.base.view.BaseRefreshView
import com.gxy.utils.network.HttpHelper

class TestRecyclePresenter : BaseRefreshPresenter<BaseRefreshView<TestRecycleData>>() {

    fun getTestRecycle(lid: String, p: Int) {
        requestRemote(
            run = {
                SystemClock.sleep(5000)
                HttpHelper.getService(TestApi::class.java).testRecycleActivity(lid, p)
            },
            success = {
                if (it == null) {
                    getView().setRecyclerViewEmptyView(R.layout.layout_empty)
                    return@requestRemote
                }
                if (it.code != 200) {
                    getView().setRecyclerViewEmptyView(R.layout.layout_error)
                    return@requestRemote
                }
                if (it.data.isEmpty()) {
                    getView().setRecyclerViewEmptyView(R.layout.layout_empty)
                    return@requestRemote
                }
                getView().setNewData(it.data)
            },
            finish = {
                getView().hideLoding()
            })
    }
}