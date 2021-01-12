package `in`.hangang.core.view.appbar.interfaces

import android.view.View

interface OnAppBarButtonClickListener {
    fun onClickViewInLeftContainer(view: View, index: Int)
    fun onClickViewInRightContainer(view: View, index: Int)
}