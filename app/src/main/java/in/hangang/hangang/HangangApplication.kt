package `in`.hangang.hangang

import android.app.Application
import android.content.Context
var isLoggable = false      //로그를 출력해도 되는지 판단하는 변수
public class HangangApplication : Application() {
    private lateinit var hangangApplicationContext: Context


    override fun onCreate() {
        super.onCreate()
        hangangApplicationContext = this
        isLoggable =isDebuggable(hangangApplicationContext)
    }

}