package `in`.hangang.core.util

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun showKeyboard(context: Context, target: View) {
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(activity: Activity) {
        activity.currentFocus?.windowToken?.let {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun Context.showKeyboard(target: View) {
    KeyboardUtil.showKeyboard(this, target)
}

fun Activity.hideKeyboard() {
    KeyboardUtil.hideKeyboard(this)
}