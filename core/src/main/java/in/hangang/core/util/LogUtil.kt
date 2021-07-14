package `in`.hangang.core.util

import android.util.Log


object LogUtil {
    private const val TAG = "LogUtil"
    var isLoggable = false      //로그를 출력해도 되는지 판단하는 변수

    /**
     * 로그 메세지를 생성하는 함수
     */
    fun buildLogMsg(message: String?): String? {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append(" [")
        sb.append(ste.className.split(".").last())
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("]")
        sb.append(message ?: "null")
        return sb.toString()
    }

    fun setIsLoggable(value: Boolean) {
        isLoggable = value
    }

    fun e(message: String?) {
        if (isLoggable) Log.e(TAG, buildLogMsg(message) ?: "")
    }

    fun w(message: String?) {
        if (isLoggable) Log.w(TAG, buildLogMsg(message)?: "")
    }

    fun i(message: String?) {
        if (isLoggable) Log.i(TAG, buildLogMsg(message)?: "")
    }

    fun d(message: String?) {
        if (isLoggable) Log.d(TAG, buildLogMsg(message)?: "")
    }

    fun v(message: String?) {
        if (isLoggable) Log.v(TAG, buildLogMsg(message)?: "")
    }


}


