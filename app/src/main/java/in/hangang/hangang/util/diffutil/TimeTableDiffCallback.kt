package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.ui.timetable.adapter.TimetableTimetablesAdapter
import androidx.recyclerview.widget.DiffUtil

class TimeTableDiffCallback(
    private val oldList: List<Pair<Int, Any?>>,
    private val newList: List<Pair<Int, Any?>>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem.first == TimetableTimetablesAdapter.TYPE_SEMESTER && newItem.first == TimetableTimetablesAdapter.TYPE_SEMESTER) {
            (oldItem.second as Int) == (newItem.second as Int)
        } else if (oldItem.first == TimetableTimetablesAdapter.TYPE_TIMETABLE && newItem.first == TimetableTimetablesAdapter.TYPE_TIMETABLE) {
            (oldItem.second as TimeTable).id == (newItem.second as TimeTable).id
        } else oldItem.first == TimetableTimetablesAdapter.TYPE_DIVIDER && newItem.first == TimetableTimetablesAdapter.TYPE_DIVIDER
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}