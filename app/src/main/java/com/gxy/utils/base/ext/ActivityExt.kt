package com.gxy.utils.base.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.orhanobut.logger.Logger

fun <T> Activity.getBundleValue(intent: Intent?, key: String, default: T): T? {
    val bundle = intent?.extras ?: return default
    val value = bundle?.get(key) ?: return default
    return value as T
}

fun <T> Activity.getBundleValue(key: String, default: T): T {
    val bundle = intent.extras ?: return default
    val value = bundle.get(key) ?: return default
    return value as T
}

fun <T : Parcelable> Activity.getBundleParcelable(intent: Intent, key: String): T? {
    val bundle = intent.extras ?: return null
    return bundle.getParcelable(key)
}

fun <T : Parcelable> Activity.getBundleParcelable(key: String): T? {
    val bundle = intent.extras ?: return null
    return bundle.getParcelable(key)
}

fun Activity.log(msg: Any?) {
    if (null == msg) {
        Logger.w("YOu print message is null")
        return
    }
//    if (BuildConfig.IS_DEBUG) {
//        Logger.e(msg.toString())
//    }
}

