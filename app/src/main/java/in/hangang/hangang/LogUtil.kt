package `in`.hangang.hangang

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log


object LogUtil {
    val TAG = "LogUtil"
    private var isLoggable = false      //로그를 출력해도 되는지 판단하는 변수

    /**
     * 로그 메세지를 생성하는 함수
     */
    fun buildLogMsg(message: String?): String? {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("[")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("]")
        sb.append(message)
        return sb.toString()
    }

    /**
     * 디버그모드인지 확인하는 함수
     */
    fun isDebuggable(context: Context): Boolean {
        var debuggable = false
        val pm: PackageManager = context.getPackageManager()
        try {
            val appinfo = pm.getApplicationInfo(context.getPackageName(), 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
        }
        isLoggable = debuggable
        return isLoggable
    }

    fun setIsLoggable(value: Boolean) {
        this.isLoggable = value
    }

    fun e(message: String) {
        if (isLoggable) Log.e(TAG, buildLogMsg(message));
    }

    fun w(message: String) {
        if (isLoggable) Log.w(TAG, buildLogMsg(message));
    }

    fun i(message: String) {
        if (isLoggable) Log.i(TAG, buildLogMsg(message));
    }

    fun d(message: String) {
        if (isLoggable) Log.d(TAG, buildLogMsg(message));
    }

    fun v(message: String) {
        if (isLoggable) Log.v(TAG, buildLogMsg(message));
    }


}


