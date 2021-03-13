package `in`.hangang.hangang.ui.timetable.adapter.viewholder

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.databinding.ItemTimetableLectureBinding
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TimetableLectureViewHolder(private val binding : ItemTimetableLectureBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : Lecture) {
        binding.lecture = item
        binding.executePendingBindings()
    }
}