package com.gxy.utils.base.presenter

import androidx.lifecycle.LifecycleOwner
import com.gxy.utils.base.view.BaseView
import com.gxy.utils.callbacks.ObserverCallBack
import com.gxy.utils.network.HttpHelper
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DataException(val code: Int, msg: String) : RuntimeException(msg) {
}

open class BasePresenter<V : BaseView> {
    private var viewRef: WeakReference<V>? = null

    protected var owner: LifecycleOwner? = null

    fun bindLifecycleOwner(owner: LifecycleOwner) {
        this.owner = owner

    }

    fun getLifecycleOwner(): LifecycleOwner = if (null == owner) {
        throw java.lang.IllegalStateException("The LifecycleOwner is null, Please bind it first...")
    } else {
        this.owner!!
    }

    fun attachView(view: V) {
        this.viewRef = WeakReference(view)
    }

    fun isAttachView(): Boolean {
        if (null == viewRef) {
            return false
        }
        return null != viewRef?.get()
    }

    open fun detachView() {
        presenterScope.cancel()
        if (isAttachView()) {
            viewRef?.clear()
            viewRef = null
        }
        owner = null
    }

    // ===Desc:=================================================================
    protected fun getView(): V {
        if (null == viewRef) {
            throw IllegalStateException("Please attach a view first")
        }
        return viewRef?.get() ?: throw IllegalStateException("Please attach a view first")
    }


    protected val presenterScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + Job())
    }

    /**
     * 协程发起一个请求,除了 runBlock 运行在 Dispatchers.IO 线程,别的都运行在主线程之中
     * @param run  运行在 Dispatchers.IO 线程
     */
    protected fun <T> requestRemote(
        befor: suspend CoroutineScope.() -> Unit = {},
        run: suspend CoroutineScope.() -> T?,
        success: (T) -> Unit = {},
        error: (Throwable) -> Unit = {},
        finish: suspend CoroutineScope.() -> Unit = {}
    ) {
        presenterScope.launch {
            try {
                befor()
                val result = run()
                if (null == result) {
                    withContext(Dispatchers.Main) {
                        error(DataException(-1, "服务器响应数据失败，请稍后重试"))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        success(result)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    if (e is SocketTimeoutException || e is SocketException || e is UnknownHostException) {
                        error(DataException(-2, "网络请求出现异常，请稍后重试"))
                    } else {
                        val message = e.message
                        if (message.isNullOrEmpty()) {
                            error(DataException(-3, "未知异常"))
                        } else {
                            error(DataException(-4, "ssssss"))
                        }
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }

    protected fun <T> createService(clazz: Class<T>) =
        HttpHelper.getService(clazz)

    /**
     * 请求服务器的方法
     */
    protected open fun <T> requestService(ob: Observable<T>, cb: ObserverCallBack<T>?) {
        ob.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(getLifecycleOwner())
            .subscribe(cb)
    }

}
