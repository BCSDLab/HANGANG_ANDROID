package `in`.hangang.core.util

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

fun View.disableClickTemporarily(){
    isClickable = false
    postDelayed({
        isClickable = true
    },500)
}
inline fun View.setOnSingleClickListener(
    interval: Long = 600L,
    crossinline action: (View) -> Unit
) {
    var clickable = true
    setOnClickListener {
        if (clickable) {
            clickable = false
            this.run {
                postDelayed({ clickable = true }, interval)
                action(this)
            }
        } else {
            LogUtil.d("Double Clicked wait for $interval")
        }
    }
}