package `in`.hangang.hangang.ui.lecturebank.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.databinding.ItemLectureBankCommentBinding
import `in`.hangang.hangang.ui.lecturebank.activity.LectureBankDetailActivity
import `in`.hangang.hangang.util.DateUtil
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankCommentsAdapter : PagingDataAdapter<LectureBankComment, LectureBankCommentsAdapter.ViewHolder>(
    lectureBankCommentDiffCallback
) {
    var onItemClickListener: OnItemClickListener? = null
    var onModifyButtonClickListener: OnModifyButtonClickListener? = null
    var lectureBankCommentEditPosition = -1
        set(value) {
            if (field >= 0) notifyItemChanged(field)
            field = value
            if (value >= 0) notifyItemChanged(value)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_lecture_bank_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(holder.absoluteAdapterPosition)?.let { lectureBankComment ->
            holder.bind(lectureBankComment)
            holder.binding.imageViewMore.setOnClickListener {
                onItemClickListener?.onMoreButtonClicked(it, lectureBankComment, holder.absoluteAdapterPosition)
            }

            with(holder.binding) {
                if (holder.absoluteAdapterPosition == lectureBankCommentEditPosition) {
                    imageViewMore.isVisible = false
                    textViewModifyComment.isVisible = true
                    textViewComment.isVisible = false
                    fieldComment.isVisible = true

                    editTextComment.filters =
                        arrayOf(InputFilter.LengthFilter(LectureBankDetailActivity.COMMENT_MAX_TEXT_COUNT))
                    editTextComment.addTextChangedListener {
                        textViewCommentTextCount.text =
                            root.context.getString(
                                R.string.lecture_bank_comment_text_count, it?.length ?: 0,
                                LectureBankDetailActivity.COMMENT_MAX_TEXT_COUNT
                            )
                        textViewModifyComment.isVisible = !it.isNullOrBlank()
                    }
                    editTextComment.setText(textViewComment.text)
                    textViewModifyComment.setOnClickListener {
                        getItem(holder.absoluteAdapterPosition)?.let { lectureBankComment ->
                            onModifyButtonClickListener?.onModifyButtonClicked(
                                it,
                                lectureBankComment,
                                editTextComment.text.toString()
                            )
                        }
                    }
                } else {
                    imageViewMore.isVisible = true
                    textViewModifyComment.isVisible = false
                    textViewComment.isVisible = true
                    fieldComment.isVisible = false
                }
            }
        }
    }

    inline fun setOnItemClickListener(crossinline onMoreButtonClicked: (View, LectureBankComment, Int) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onMoreButtonClicked(view: View, lectureBankComment: LectureBankComment, position: Int) {
                onMoreButtonClicked(view, lectureBankComment, position)
            }
        }
    }

    inline fun setOnModifyButtonClickListener(crossinline onModifyButtonClick: (View, LectureBankComment, String) -> Unit) {
        onModifyButtonClickListener = object : OnModifyButtonClickListener {
            override fun onModifyButtonClicked(view: View, lectureBankComment: LectureBankComment, comment: String) {
                onModifyButtonClick(view, lectureBankComment, comment)
            }
        }
    }

    class ViewHolder(
        val binding: ItemLectureBankCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lectureBankComment: LectureBankComment) {
            binding.comment = lectureBankComment
            binding.textViewDate.text = DateUtil.apiDateToPeriodString(
                binding.root.context,
                lectureBankComment.createdAt ?: DateUtil.getTodayApiDate()
            )
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onMoreButtonClicked(view: View, lectureBankComment: LectureBankComment, position: Int)
    }

    interface OnModifyButtonClickListener {
        fun onModifyButtonClicked(view: View, lectureBankComment: LectureBankComment, comment: String)
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