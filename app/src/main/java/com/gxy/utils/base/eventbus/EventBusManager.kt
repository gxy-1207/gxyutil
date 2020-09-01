package com.core.ui.eventbus

import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus

object EventBusManager {

    /**注册EventBus*/
    fun register(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            Logger.i("The subscriber '${subscriber.javaClass.simpleName}' has already register to EventBus")
            return
        }
        EventBus.getDefault().register(subscriber)
    }

    /**反注册EventBus*/
    fun unRegister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    fun obtain(what: Int): EventBusMessage {
        val msg = EventBusMessage()
        msg.what = what
        return msg
    }

    fun postSticky(msg: EventBusMessage) {
        EventBus.getDefault().postSticky(msg)
    }

    fun post(msg: EventBusMessage) {
        EventBus.getDefault().post(msg)
    }

    fun postEmptySticky(what: Int) {
        EventBus.getDefault().postSticky(obtain(what))
    }

    fun postEmpty(what: Int) {
        EventBus.getDefault().post(obtain(what))
    }
}