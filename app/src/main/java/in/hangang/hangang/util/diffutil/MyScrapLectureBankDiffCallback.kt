package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import androidx.recyclerview.widget.DiffUtil

class MyScrapLectureBankDiffCallback(
    private val oldList: List<LectureBankScrap>,
    private val newList: List<LectureBankScrap>
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