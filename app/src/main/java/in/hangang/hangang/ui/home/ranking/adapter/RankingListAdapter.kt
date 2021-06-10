package `in`.hangang.hangang.ui.home.ranking.adapter

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.ItemHomeRankingListBinding
import `in`.hangang.hangang.util.LogUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RankingListAdapter :
    ListAdapter<RankingLectureItem, RankingListAdapter.ViewHolder>(rankingLectureDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeRankingListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rankngLectureItem = getItem(position)
        if (rankngLectureItem != null) {
            holder.bind(rankngLectureItem, position)
        }else{
            LogUtil.e("onbindviewholder")
        }
    }


    class ViewHolder(private val binding: ItemHomeRankingListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
        fun bind(item: RankingLectureItem, position: Int) {
            binding.apply {
                rankingLectureItem = item
                ranking = position + 1
                executePendingBindings()
            }
        }
    }

    companion object {
        val rankingLectureDiffUtil = object : DiffUtil.ItemCallback<RankingLectureItem>() {
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