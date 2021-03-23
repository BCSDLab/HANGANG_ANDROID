package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.ItemTimetableLectureBinding
import `in`.hangang.hangang.util.TimetableUtil
import `in`.hangang.hangang.util.diffutil.LectureDiffCallback
import `in`.hangang.hangang.util.diffutil.LectureTimeTableDiffCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableLectureAdapter(private val context: Context) : RecyclerView.Adapter<TimetableLectureAdapter.TimetableLectureViewHolder>() {

    private val lectures = mutableListOf<LectureTimeTable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableLectureViewHolder {
        return TimetableLectureViewHolder(
                DataBindingUtil.bind(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_timetable_lecture, parent, false)
                )!!
        )
    }

    override fun onBindViewHolder(holder: TimetableLectureViewHolder, position: Int) {
        holder.bind(lectures[position])
    }

    override fun getItemCount(): Int = lectures.size

    fun updateItem(lectures: List<LectureTimeTable>) {
        val diffCallback = LectureTimeTableDiffCallback(this.lectures, lectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class TimetableLectureViewHolder(private val binding: ItemTimetableLectureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LectureTimeTable) {
            binding.lecture = item
            binding.textViewLectureTime.text = TimetableUtil.toString(context, item.classTime ?: "[]")
            binding.executePendingBindings()
        }
    }
}