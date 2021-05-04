package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.ItemHomeRankingListBinding
import `in`.hangang.hangang.databinding.ItemLectureReviewBinding
import `in`.hangang.hangang.util.LogUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LectureReviewAdapter(var scrapList: ArrayList<RankingLectureItem>) :
    PagingDataAdapter<RankingLectureItem, LectureReviewAdapter.ViewHolder>(lectureReviewDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLectureReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rankngLectureItem = getItem(position)
        if (rankngLectureItem != null) {
            holder.bind(rankngLectureItem)
            if(scrapList.contains(rankngLectureItem)){
                holder.binding.lectureReviewScrap.visibility = View.VISIBLE
            }else{
                holder.binding.lectureReviewScrap.visibility = View.INVISIBLE
            }
        }else{
            LogUtil.e("onbindviewholder")
        }
    }


    class ViewHolder(val binding: ItemLectureReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RankingLectureItem) {
            binding.apply {
                lectureReviewItem = item
                executePendingBindings()
            }
        }
    }

    companion object {
        val lectureReviewDiffUtil = object : DiffUtil.ItemCallback<RankingLectureItem>() {
            override fun areItemsTheSame(
                oldItem: RankingLectureItem,
                newItem: RankingLectureItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RankingLectureItem,
                newItem: RankingLectureItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}