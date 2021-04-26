package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.LectureTimeTable
import androidx.recyclerview.widget.DiffUtil

class LectureTimeTableDiffCallback(
    private val oldList: List<LectureTimeTable>,
    private val newList: List<LectureTimeTable>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}