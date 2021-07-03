package `in`.hangang.hangang.ui.home.ranking.adapter

import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.ItemHomeRankingListBinding
import `in`.hangang.hangang.util.LogUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RankingListAdapter :
    ListAdapter<RankingLectureItem, RankingListAdapter.ViewHolder>(rankingLectureDiffUtil) {
    lateinit var lectureClickListener: RecyclerViewClickListener
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
        val rankingLectureItem = getItem(position)
        if (rankingLectureItem != null) {
            holder.bind(rankingLectureItem, position)
            holder.binding.rankingItemConstraintlayout.setOnClickListener {
                lectureClickListener.onClick(it, position, rankingLectureItem)
            }
        }else{
            LogUtil.e("onbindviewholder")
        }
    }
    fun setClickListener(listener: RecyclerViewClickListener) {
        lectureClickListener = listener
    }

    class ViewHolder(val binding: ItemHomeRankingListBinding) :
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