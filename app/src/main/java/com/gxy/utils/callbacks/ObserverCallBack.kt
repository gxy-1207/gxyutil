package com.gxy.utils.callbacks

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

interface ObserverCallBack<T> : Observer<T> {

    override fun onSubscribe(d: Disposable?) {
        onRequestStart(d)
    }

    override fun onNext(t: T) {
        onRequestSuccess(t)
    }

    override fun onError(e: Throwable?) {
        onRequestFailed(e ?: IllegalStateException("UNKNOW ERROR"))
        onRequestFinish()
    }

    override fun onComplete() {
        onRequestFinish()
    }

    // ===Desc:=================================================================
    /**
     * 网络请求开始之前
     * */
    fun onRequestStart(d: Disposable?) {}

    /**
     * 网络请求成功
     * */
    fun onRequestSuccess(data: T) {}

    /**
     * 网络请求错误
     * */
    fun onRequestFailed(error: Throwable) {}

    /**
     * 网络请求结束执行
     * */
    fun onRequestFinish() {}
}