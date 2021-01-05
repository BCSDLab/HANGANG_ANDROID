package `in`.hangang.core.toast

import android.content.Context
import android.widget.Toast

inline fun Context.shortToast(message: () -> String) {
    Toast.makeText(this, message(), Toast.LENGTH_SHORT).show()
}
inline fun Context.longToast(message: () -> String) {
    Toast.makeText(this, message(), Toast.LENGTH_LONG).show()
}