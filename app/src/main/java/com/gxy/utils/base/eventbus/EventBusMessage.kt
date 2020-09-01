package com.core.ui.eventbus

class EventBusMessage {
    var what: Int = -1

    var obj: Any? = null
    var list = mutableListOf<Any>()
    var arg1: Int = 0
    var arg2: Int = 0
    var arg3: Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventBusMessage

        if (what != other.what) return false
        if (obj != other.obj) return false
        if (list != other.list) return false
        if (arg1 != other.arg1) return false
        if (arg2 != other.arg2) return false
        if (arg3 != other.arg3) return false

        return true
    }

    override fun hashCode(): Int {
        var result = what
        result = 31 * result + (obj?.hashCode() ?: 0)
        result = 31 * result + list.hashCode()
        result = 31 * result + arg1
        result = 31 * result + arg2
        result = 31 * result + arg3
        return result
    }

    override fun toString(): String {
        return "EventBusMessage(what=$what, obj=$obj, list=$list, arg1=$arg1, arg2=$arg2, arg3=$arg3)"
    }


}