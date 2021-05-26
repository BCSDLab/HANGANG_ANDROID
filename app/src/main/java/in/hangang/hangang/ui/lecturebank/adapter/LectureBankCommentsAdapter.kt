package `in`.hangang.hangang.ui.lecturebank.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.databinding.ItemLectureBankCommentBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankCommentsAdapter : PagingDataAdapter<LectureBankComment, LectureBankCommentsAdapter.ViewHolder>(
    lectureBankCommentDiffCallback
) {
    var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lecture_bank_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { lectureBankComment ->
            holder.bind(lectureBankComment)
            holder.binding.textViewReport.setOnClickListener {
                onItemClickListener?.onReportButtonClicked(lectureBankComment)
            }
        }
    }

    inline fun setOnItemClickListener(crossinline onReportButtonClicked : (LectureBankComment) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onReportButtonClicked(lectureBankComment: LectureBankComment) {
                onReportButtonClicked(lectureBankComment)
            }
        }
    }

    class ViewHolder(
        val binding: ItemLectureBankCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lectureBankComment: LectureBankComment) {
            binding.comment = lectureBankComment
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onReportButtonClicked(lectureBankComment : LectureBankComment)
    }

    companion object {
        private val lectureBankCommentDiffCallback = object : DiffUtil.ItemCallback<LectureBankComment>() {
            override fun areItemsTheSame(oldItem: LectureBankComment, newItem: LectureBankComment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LectureBankComment, newItem: LectureBankComment): Boolean {
                return oldItem == newItem
            }
        }
    }
}