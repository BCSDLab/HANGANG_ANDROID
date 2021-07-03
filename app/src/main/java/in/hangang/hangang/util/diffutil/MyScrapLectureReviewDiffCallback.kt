package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.Lecture
import androidx.recyclerview.widget.DiffUtil

class MyScrapLectureReviewDiffCallback(
    private val oldList: List<Lecture>,
    private val newList: List<Lecture>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}