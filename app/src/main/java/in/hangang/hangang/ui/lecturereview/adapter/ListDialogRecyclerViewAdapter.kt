package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.core.databinding.ListDialogItemBinding
import `in`.hangang.core.util.LogUtil
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.evaluation.LectureDoc
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ListDialogRecyclerViewAdapter :
    ListAdapter<TimeTable, ListDialogRecyclerViewAdapter.ViewHolder>(timetableDiffUtil){
    private lateinit var timeTableClickListener: RecyclerViewClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeTable = getItem(position)
        if(timeTable != null){
            holder.bind(timeTable)
        }else{
            LogUtil.e("error")
        }
        var targetPosition = position
        holder.binding.listDialogCheck.setOnClickListener { v->
            timeTableClickListener.onClick(v, targetPosition, timeTable)
        }
    }
    fun setCheckClickListener(recyclerViewClickListener: RecyclerViewClickListener){
        this.timeTableClickListener = recyclerViewClickListener
    }


    class ViewHolder(val binding: ListDialogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeTable){
            binding.apply {
                name = item.name
                isChecked = item.isChecked
                executePendingBindings()
            }
        }
    }
    companion object {
        val timetableDiffUtil = object : DiffUtil.ItemCallback<TimeTable>(){
            override fun areItemsTheSame(oldItem: TimeTable, newItem: TimeTable): Boolean {
                return oldItem.isChecked == newItem.isChecked
            }

            override fun areContentsTheSame(oldItem: TimeTable, newItem: TimeTable): Boolean {
                return oldItem.isChecked == newItem.isChecked
            }
        }
    }
}