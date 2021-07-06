package `in`.hangang.core.util

import android.os.Build
import android.os.Build.VERSION_CODES.Q

inline fun requireAndroidVersion(version: Int, func: () -> Unit) {
    if(Build.VERSION.SDK_INT >= version) {
        func()
    }
}

inline fun requireQ(func: () -> Unit) {
    requireAndroidVersion(Build.VERSION_CODES.Q) {
        func()
    }
}