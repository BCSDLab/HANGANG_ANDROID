package `in`.hangang.hangang

import android.app.Application
import android.content.Context

public class HangangApplication : Application() {
    private lateinit var hangangApplicationContext: Context


    override fun onCreate() {
        super.onCreate()
        hangangApplicationContext = this
        LogUtil.isApplicationDebug(hangangApplicationContext)
    }

}