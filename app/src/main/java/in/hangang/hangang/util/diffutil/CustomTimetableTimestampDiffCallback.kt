package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.Lecture
import androidx.recyclerview.widget.DiffUtil

class CustomTimetableTimestampDiffCallback(
    private val oldList : List<CustomTimetableTimestamp>,
    private val newList : List<CustomTimetableTimestamp>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}