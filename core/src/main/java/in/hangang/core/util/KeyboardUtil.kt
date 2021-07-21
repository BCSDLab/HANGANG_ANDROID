package `in`.hangang.core.util

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.showKeyboard(target: View) {
    val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard() {
    val inputManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView: View? = currentFocus
    if (currentFocusedView != null) {
        inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}