package com.gxy.utils.test

import com.gxy.utils.apis.TestApi
import com.gxy.utils.base.presenter.BasePresenter
import com.gxy.utils.callbacks.ObserverCallBack
import com.gxy.utils.utils.LoggerUtil
import io.reactivex.rxjava3.disposables.Disposable

class TestPresenter : BasePresenter<TestView>() {
    fun getTestActivity(uid: String, id: String) {
        val ob = createService(TestApi::class.java).testActivity(uid, id)
        requestService(ob, object : ObserverCallBack<TestBean> {
            override fun onRequestStart(d: Disposable?) {
                getView().showLoding()
            }

            override fun onRequestSuccess(data: TestBean) {
                if (data == null) {
                    getView().showEmpty()
                    return
                }
                if (data.status != 200) {
                    getView().showError()
                    return
                }
//                getView().showSuccess()
                getView().onTestSuccess(data)
            }

            override fun onRequestFailed(error: Throwable) {
                getView().showError()
                LoggerUtil.Log(error)
            }

            override fun onRequestFinish() {
                getView().hideLoding()
            }
        })
    }
}