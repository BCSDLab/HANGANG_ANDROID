package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.ItemHomeRankingListBinding
import `in`.hangang.hangang.databinding.ItemLectureReviewBinding
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LectureReviewAdapter(val context: Context) :
    PagingDataAdapter<RankingLectureItem, LectureReviewAdapter.ViewHolder>(lectureReviewDiffUtil) {
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
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
        val rankingLectureItem = getItem(position)
        if (rankingLectureItem != null) {
            holder.bind(rankingLectureItem)
        }else{
            LogUtil.e("onbindviewholder")
        }
        holder.binding.lectureReviewConstraintlayout.setOnClickListener {
            var bundle = Bundle()
            bundle.putParcelable("lecture",rankingLectureItem)
            navController.navigate(R.id.action_navigation_evaluation_to_lecture_review_detail, bundle)
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