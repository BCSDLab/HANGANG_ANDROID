package `in`.hangang.hangang

import `in`.hangang.hangang.util.LogUtil
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class HangangApplication : Application() {
    private lateinit var hangangApplicationContext: Context

    override fun onCreate() {
        super.onCreate()
        hangangApplicationContext = this
        LogUtil.isLoggable.apply {
            isApplicationDebug(hangangApplicationContext)
        }
    }


    /**
     * 디버그모드인지 확인하는 함수
     */
    fun isApplicationDebug(context: Context): Boolean {
        var debuggable = false
        val pm: PackageManager = context.getPackageManager()
        try {
            val appinfo = pm.getApplicationInfo(context.getPackageName(), 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return debuggable
    }

}