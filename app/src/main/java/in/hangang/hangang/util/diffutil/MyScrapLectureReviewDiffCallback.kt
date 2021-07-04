package `in`.hangang.hangang.util.diffutil

import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import androidx.recyclerview.widget.DiffUtil

class MyScrapLectureReviewDiffCallback(
    private val oldList: List<RankingLectureItem>,
    private val newList: List<RankingLectureItem>
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