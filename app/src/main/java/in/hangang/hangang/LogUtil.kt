package `in`.hangang.hangang

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log


class LogUtil {
    private val TAG = "HangangLog"

    fun e(message: String?) {
        if (HangangApplication.DEBUG) Log.e(TAG, buildLogMsg(message))
    }

    fun w(message: String?) {
        if (HangangApplication.DEBUG) Log.w(TAG, buildLogMsg(message))
    }

    fun i(message: String?) {
        if (HangangApplication.DEBUG) Log.i(TAG, buildLogMsg(message))
    }

    fun d(message: String?) {
        if (HangangApplication.DEBUG) Log.d(TAG, buildLogMsg(message))
    }

    fun v(message: String?) {
        if (HangangApplication.DEBUG) Log.v(TAG, buildLogMsg(message))
    }


    fun isDebuggable(context: Context): Boolean {
        var debuggable = false
        val pm: PackageManager = context.getPackageManager()
        try {
            val appinfo = pm.getApplicationInfo(context.getPackageName(), 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return debuggable
    }


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
}