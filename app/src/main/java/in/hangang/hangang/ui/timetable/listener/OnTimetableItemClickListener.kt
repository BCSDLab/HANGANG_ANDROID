package `in`.hangang.hangang.ui.timetable.listener

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)
    fun onDeleteButtonClick(view: View, position: Int)
}