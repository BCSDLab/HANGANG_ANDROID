package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.ui.timetable.adapter.TimetableTimetablesAdapter
import androidx.recyclerview.widget.DiffUtil

class TimeTableDiffCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is Int && newItem is Int -> oldItem == newItem
            oldItem is TimeTable && newItem is TimeTable -> oldItem.id == newItem.id
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}