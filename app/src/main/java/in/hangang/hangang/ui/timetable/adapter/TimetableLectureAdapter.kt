package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.core.view.goneVisible
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.ItemTimetableLectureBinding
import `in`.hangang.hangang.ui.timetable.listener.TimetableLectureListener
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
    private val selectedLectures = mutableListOf<LectureTimeTable>()
    var currentSelectedPosition = -1

    var timetableLectureListener : TimetableLectureListener? = null

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

        holder.itemView.isSelected = position == currentSelectedPosition
        holder.setSelected(selectedLectures.contains(lectures[position]))

        holder.itemView.setOnClickListener {
            val beforeSelectedPosition = currentSelectedPosition
            currentSelectedPosition = position

            notifyItemChanged(beforeSelectedPosition)
            notifyItemChanged(currentSelectedPosition)

            timetableLectureListener?.onCheckedChange(position, lectures[position])
        }

        holder.binding.buttonAddLecture.setOnClickListener {
            if(timetableLectureListener?.onAddButtonClicked(position, lectures[position]) == true)
                holder.setSelected(true)
        }
        holder.binding.buttonRemoveLecture.setOnClickListener {
            if(timetableLectureListener?.onRemoveButtonClicked(position, lectures[position]) == true)
                holder.setSelected(false)
        }
        holder.binding.buttonLectureReview.setOnClickListener {
            timetableLectureListener?.onReviewButtonClicked(position, lectures[position])
        }
    }

    override fun getItemCount(): Int = lectures.size

    fun updateItem(lectures: List<LectureTimeTable>) {
        val diffCallback = LectureTimeTableDiffCallback(this.lectures, lectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSelectedLectures(selectedLectures: List<LectureTimeTable>) {
        val diffCallback = LectureTimeTableDiffCallback(this.selectedLectures, selectedLectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.selectedLectures.clear()
        this.selectedLectures.addAll(selectedLectures)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class TimetableLectureViewHolder(val binding: ItemTimetableLectureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LectureTimeTable) {
            binding.textViewLectureTitle.text = item.name
            binding.textViewLectureCode.text = item.code
            binding.textViewLectureStar.text = String.format("%1.1f", item.rating)
            binding.textViewLectureProfessor.text = "${item.professor} (${item.classNumber})"
            binding.textViewLectureCredit.text = context.getString(R.string.credit, item.designScore)
            binding.textViewLectureGrade.text = context.getString(R.string.grade, item.grades)
            binding.textViewLectureClassification.text = item.classification
            binding.textViewLectureTime.text = TimetableUtil.toString(context, item.classTime ?: "[]")
        }

        fun setSelected(isSelected: Boolean) {
            binding.buttonAddLecture.visibility = goneVisible(isSelected)
            binding.buttonRemoveLecture.visibility = visibleGone(isSelected)
        }
    }
}