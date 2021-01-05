package `in`.hangang.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

open class ActivityBase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}