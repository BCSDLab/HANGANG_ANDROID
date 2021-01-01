package `in`.hangang.hangang

import android.app.Application
import android.content.Context

public class HangangApplication : Application() {
    private lateinit var hangangApplicationContext: Context

    companion object{
        var DEBUG = true
    }
    override fun onCreate() {
        super.onCreate()
        hangangApplicationContext = this
        DEBUG = LogUtil().isDebuggable(hangangApplicationContext)
    }

}