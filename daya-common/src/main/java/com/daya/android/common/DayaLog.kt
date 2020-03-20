package com.daya.android.common

import android.util.Log

object DayaLog {
    var prefix: String? = "Daya"

    fun d(tag: String? = null, msg: String) {
        println(Log.DEBUG, tag, msg)
    }

    fun e(tag: String? = null, msg: String, tr: Throwable? = null) {
        val newMsg = tr?.let { "$msg\n${Log.getStackTraceString(it)}" } ?: msg
        println(Log.ERROR, tag, newMsg)
    }

    private fun println(priority: Int, tag: String? = null, msg: String) {
        val newMsg = prefix?.let { "[$it] $msg" } ?: msg
        Log.println(priority, tag, newMsg)
    }
}

