package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.evaluation.LectureReview
import `in`.hangang.hangang.databinding.ItemLectureDetailReviewBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LectureDetailReviewAdapter: PagingDataAdapter<LectureReview, LectureDetailReviewAdapter.ViewHolder>(
    lectureDetailReviewDiffUtil)  {
    private lateinit var thumUpClickListner: RecyclerViewClickListener
    private lateinit var reportClickListener: RecyclerViewClickListener
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lectureReview = getItem(position)
        if(lectureReview != null){
            holder.bind(lectureReview)
            holder.binding.lectureDetailThumbUp.setOnClickListener { v ->
                if (thumUpClickListner != null) {
                    thumUpClickListner.onClick(v, position, lectureReview)
                }
            }
            holder.binding.lectureDetailReport.setOnClickListener { v ->
                if(reportClickListener != null){
                    reportClickListener.onClick(v, position, lectureReview)
                }
            }
        }
    }
    fun setRecyclerViewListener(recyclerviewClickListner: RecyclerViewClickListener){
        this.thumUpClickListner = recyclerviewClickListner
    }
    fun setReportClickListener(recyclerviewClickListner: RecyclerViewClickListener){
        this.reportClickListener = recyclerviewClickListner
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLectureDetailReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(val binding: ItemLectureDetailReviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: LectureReview){
            binding.apply {
                lectureReviewItem = item
                lectureDetailStarRatingView.setRatingStar(item.rating)
                if(item.isLiked)
                    lectureDetailThumbUp.setImageResource(R.drawable.ic_thumb_up_check)
                else
                    lectureDetailThumbUp.setImageResource(R.drawable.ic_thumb_up_uncheck)
                executePendingBindings()
            }
        }
    }
    companion object {
        val lectureDetailReviewDiffUtil = object : DiffUtil.ItemCallback<LectureReview>() {
            override fun areItemsTheSame(
                oldItem: LectureReview,
                newItem: LectureReview
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LectureReview,
                newItem: LectureReview
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}