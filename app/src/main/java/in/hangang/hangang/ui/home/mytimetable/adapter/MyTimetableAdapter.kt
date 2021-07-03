package `in`.hangang.hangang.ui.home.mytimetable.adapter

import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.databinding.ItemHomeMyTimetableListBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MyTimetableAdapter :
    ListAdapter<LectureTimeTable, MyTimetableAdapter.ViewHolder>(timeTableDiffUtil) {
    lateinit var myTimeTableClickListener: RecyclerViewClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeMyTimetableListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeTable = getItem(position)
        if(timeTable != null) {
            holder.bind(timeTable)
            holder.binding.itemMyTimetableConstraintlayout.setOnClickListener {
                myTimeTableClickListener.onClick(it as ConstraintLayout, position, timeTable as LectureTimeTable)
            }
            if(holder.binding.itemMyTimetableButtonEvaluate.isEnabled) {
                holder.binding.itemMyTimetableButtonEvaluate.setOnClickListener {
                    myTimeTableClickListener.onClick(it as RoundedCornerButton, position, timeTable as LectureTimeTable)
                }
            }
        }
    }
    fun setClickListener(listener: RecyclerViewClickListener) {
        myTimeTableClickListener = listener
    }
    class ViewHolder(val binding: ItemHomeMyTimetableListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LectureTimeTable) {
            binding.apply {
                timetable = item
                executePendingBindings()
            }
        }
    }

    companion object {
        val timeTableDiffUtil = object : DiffUtil.ItemCallback<LectureTimeTable>() {
            override fun areItemsTheSame(
                oldItem: LectureTimeTable,
                newItem: LectureTimeTable
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LectureTimeTable,
                newItem: LectureTimeTable
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}