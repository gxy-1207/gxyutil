package com.gxy.utils.network

import androidx.lifecycle.LifecycleOwner
import com.gxy.utils.callbacks.ObserverCallBack
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @Author:         gxy
 * @CreateDate:     2020/8/28
 * @Description:    网络请求工具类
 */
object HttpHelper {
    private lateinit var baseUrl: String
    private lateinit var retrofit: Retrofit
    fun init(baseUrl: String, interceptors: List<Interceptor>) {
        this.baseUrl = baseUrl
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initOkHttpClient(interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    /** 初始化OkHttpClient  */
    private fun initOkHttpClient(interceptors: List<Interceptor>): OkHttpClient {
        val mClientBuilder = OkHttpClient.Builder()
        mClientBuilder
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
        for (item in interceptors) {
            mClientBuilder.addInterceptor(item)
        }
        return mClientBuilder.build()
    }

    fun <T> getService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    /** 请求入列  */
    fun <T> enqueue(
        observable: Observable<T>, owner: LifecycleOwner, cb: ObserverCallBack<T>? = null
    ) {
        observable
            .subscribeOn(Schedulers.newThread())
            .bindToLifecycle(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cb)
    }
}

//fun <T> Observable<T>.en(owner: LifecycleOwner, cb: Observer<T>) {
//    this.subscribeOn(Schedulers.newThread())
//        .bindToLifecycle(owner)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(cb)
//}