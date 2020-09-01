package com.core.ui.eventbus

import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

interface EventBusHandler {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEventBusMessage(msg: EventBusMessage) {
        Logger.e("Receive EventBus message at '${javaClass.simpleName}', message what is : ${msg.what}")
        receiveEventBusMessage(msg)
    }

    /**接收到EventBus处理*/
    fun receiveEventBusMessage(msg: EventBusMessage) {

    }
}