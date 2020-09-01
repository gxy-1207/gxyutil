package com.gxy.utils.base.ext

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger

fun Fragment.log(msg: Any?) {
    if (null == msg) {
        Logger.w("YOu print message is null")
        return
    }
//    if (BuildConfig.IS_DEBUG) {
//        Logger.e(msg.toString())
//    }
}

//===Desc:================================================================================


fun <T> Fragment.getArgumentsValue(
    @NonNull key: String,
    defaultValue: T
): T {
    val bundle = arguments ?: return defaultValue
    val value = bundle.get(key) ?: return defaultValue
    @Suppress("UNCHECKED_CAST")
    return value as T
}

fun <T : Parcelable?> Fragment.getArgumentsParcelable(
    @NonNull key: String
): T? {
    if (null == arguments) {
        return null
    }
    return arguments?.getParcelable<T>(key)
}