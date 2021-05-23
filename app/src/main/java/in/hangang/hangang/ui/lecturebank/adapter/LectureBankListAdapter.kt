package `in`.hangang.hangang.ui.lecturebank.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.databinding.ItemLectureBankBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LectureBankListAdapter : PagingDataAdapter<LectureBank, LectureBankListAdapter.LectureBankViewHolder>(
    lectureBankDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureBankViewHolder {
        return LectureBankViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lecture_bank, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LectureBankViewHolder, position: Int) {
        val lectureBank = getItem(position)
        if(lectureBank != null) {
            holder.bind(lectureBank)
        }
    }

    class LectureBankViewHolder(
        private val binding : ItemLectureBankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lectureBank: LectureBank) {
            binding.root.clipToOutline = true
            binding.bank = lectureBank
            Glide.with(binding.root.context).load(lectureBank.thumbnail).into(binding.imageViewLectureBankThumbnail)

            binding.executePendingBindings()
        }
    }

    companion object {
        val lectureBankDiffUtil = object : DiffUtil.ItemCallback<LectureBank>() {
            override fun areItemsTheSame(oldItem: LectureBank, newItem: LectureBank): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LectureBank, newItem: LectureBank): Boolean {
                return oldItem == newItem
            }
        }
    }
}