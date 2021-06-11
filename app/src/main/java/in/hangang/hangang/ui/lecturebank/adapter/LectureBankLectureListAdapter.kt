package `in`.hangang.hangang.ui.lecturebank.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.databinding.ItemLectureBankLectureListBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankLectureListAdapter : PagingDataAdapter<Lecture, LectureBankLectureListAdapter.ViewHolder>(
    lectureDiffUtil
) {

    var selectedLecture: Lecture? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_lecture_bank_lecture_list,
                    parent,
                    false
                )
            )!!
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lecture = getItem(position)
        if (lecture != null) {
            holder.bind(lecture)
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(
                    holder.absoluteAdapterPosition,
                    lecture,
                    selectedLecture == lecture
                )
            }
        }
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Int, Lecture, Boolean) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, lecture: Lecture, isSelected: Boolean) {
                onItemClick(position, lecture, isSelected)
            }
        }
    }

    class ViewHolder(val binding: ItemLectureBankLectureListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lecture: Lecture, isSelected: Boolean = false) {
            binding.lecture = lecture
            binding.buttonSelectLecture.isEnabled = isSelected
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, lecture: Lecture, isSelected: Boolean)
    }

    companion object {
        private val lectureDiffUtil = object : DiffUtil.ItemCallback<Lecture>() {
            override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
                return oldItem == newItem
            }

        }
    }
}