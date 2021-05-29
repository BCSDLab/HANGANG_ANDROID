package `in`.hangang.hangang

import `in`.hangang.hangang.di.*
import `in`.hangang.hangang.util.LogUtil
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class HangangApplication : Application() {
    private val hangangApplicationContext: Context = this
    val isApplicationDebug
        get() = isApplicationDebug(hangangApplicationContext)

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(hangangApplicationContext).build()
        LogUtil.isLoggable = isApplicationDebug
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        startKoin {
            androidContext(this@HangangApplication)
            modules(netWorkModule)
            modules(dataSourceModule)
            modules(repositoryModule)
            modules(viewModelModule)
            modules(fileModule)
        }
    }

    /**
     * 디버그모드인지 확인하는 함수
     */
    private fun isApplicationDebug(context: Context): Boolean {
        var debuggable = false
        val pm: PackageManager = context.packageManager
        try {
            val appinfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return debuggable
    }

    companion object {
        lateinit var instance: HangangApplication
            private set
    }
}