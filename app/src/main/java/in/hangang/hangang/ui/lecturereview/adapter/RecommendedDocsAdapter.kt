package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.databinding.ItemRecommendedLectureDocsBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecommendedDocsAdapter :
    ListAdapter<LectureDoc, RecommendedDocsAdapter.ViewHolder>(lectureDocDiffUtil){
    lateinit var itemClickListener: RecyclerViewClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecommendedLectureDocsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lectureDoc = getItem(position)
        if(lectureDoc != null){
            holder.bind(lectureDoc)
            holder.binding.lectureReviewConstraintlayout.setOnClickListener {
                itemClickListener.onClick(it,position, lectureDoc)
            }
        }
    }
    fun setLectureDocClickListener(listener: RecyclerViewClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecommendedLectureDocsBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(item: LectureDoc){
                    binding.apply {
                        lectureDoc = item
                        executePendingBindings()
                    }
                }
            }
    companion object{
        val lectureDocDiffUtil = object : DiffUtil.ItemCallback<LectureDoc>(){
            override fun areItemsTheSame(oldItem: LectureDoc, newItem: LectureDoc): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LectureDoc, newItem: LectureDoc): Boolean {
                return oldItem == newItem
            }
        }
    }
}