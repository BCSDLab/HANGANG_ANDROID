package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.core.view.goneVisible
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.ItemTimetableLectureBinding
import `in`.hangang.hangang.ui.timetable.listener.TimetableLectureListener
import `in`.hangang.hangang.util.TimetableUtil
import `in`.hangang.hangang.util.diffutil.LectureTimeTableDiffCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableLectureAdapter(private val context: Context) : RecyclerView.Adapter<TimetableLectureAdapter.TimetableLectureViewHolder>() {

    private val lectures = mutableListOf<LectureTimeTable>()
    private val addedLectures = mutableSetOf<LectureTimeTable>()
    private val scraps = mutableListOf<LectureTimeTable>()
    var currentSelectedPosition = -1

    var timetableLectureListener: TimetableLectureListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableLectureViewHolder {
        return TimetableLectureViewHolder(
                DataBindingUtil.bind(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_timetable_lecture, parent, false)
                )!!
        )
    }

    override fun onBindViewHolder(holder: TimetableLectureViewHolder, position: Int) {
        val item = lectures[position]
        holder.bind(item)

        holder.itemView.isSelected = position == currentSelectedPosition
        if (addedLectures.contains(item))
            holder.setSelected(true)
        else
            holder.setSelected(false)
        holder.setScrapButton(scraps.contains(item))

        holder.itemView.setOnClickListener {
            val beforeSelectedPosition = currentSelectedPosition

            if (currentSelectedPosition == position) {
                timetableLectureListener?.onCheckedChange(-1, item)
                currentSelectedPosition = -1
            } else {
                timetableLectureListener?.onCheckedChange(position, item)
                currentSelectedPosition = position
            }

            notifyItemChanged(beforeSelectedPosition)
            notifyItemChanged(currentSelectedPosition)
        }

        holder.binding.buttonAddLecture.setOnClickListener {
            if (timetableLectureListener?.onAddButtonClicked(position, item) == true)
                holder.setSelected(true)
        }
        holder.binding.buttonRemoveLecture.setOnClickListener {
            if (timetableLectureListener?.onRemoveButtonClicked(position, item) == true)
                holder.setSelected(false)
        }
        holder.binding.buttonLectureReview.setOnClickListener {
            timetableLectureListener?.onReviewButtonClicked(position, item)
        }
        holder.binding.checkBoxScrap.setOnClickListener {
            timetableLectureListener?.onScrapButtonClicked(position, item)
        }
    }

    override fun getItemCount(): Int = lectures.size

    fun updateItem(lectures: Collection<LectureTimeTable>) {
        val diffCallback = LectureTimeTableDiffCallback(this.lectures, lectures.toList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSelectedLectures(selectedLectures: Collection<LectureTimeTable>) {
        this.addedLectures.clear()
        this.addedLectures.addAll(selectedLectures)

        notifyDataSetChanged()
    }

    fun updateScrapedLecture(scraps: Collection<LectureTimeTable>) {
        this.scraps.clear()
        this.scraps.addAll(scraps)

        notifyDataSetChanged()
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
            binding.textViewLectureTime.text = TimetableUtil.toString(context, item.classTime
                    ?: "[]")
        }

        fun setSelected(isSelected: Boolean) {
            binding.buttonAddLecture.visibility = goneVisible(isSelected)
            binding.buttonRemoveLecture.visibility = visibleGone(isSelected)
        }

        fun setScrapButton(isSelected: Boolean) {
            binding.checkBoxScrap.setImageDrawable(
                    ContextCompat.getDrawable(context,
                            if (isSelected) R.drawable.ic_check_v_selected
                            else R.drawable.ic_check_v_unselected
                    )
            )
        }
    }
}