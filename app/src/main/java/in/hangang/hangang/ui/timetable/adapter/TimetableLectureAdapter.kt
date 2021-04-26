package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.core.view.setVisibility
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.TIMETABLE_EMPTY_POSITION
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.ItemTimetableLectureBinding
import `in`.hangang.hangang.ui.timetable.listener.TimetableLectureListener
import `in`.hangang.hangang.util.diffutil.LectureTimeTableDiffCallback
import `in`.hangang.hangang.util.timetable.TimetableUtil
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableLectureAdapter(private val context: Context) :
    RecyclerView.Adapter<TimetableLectureAdapter.TimetableLectureViewHolder>() {

    private val lectures = mutableListOf<LectureTimeTable>()
    private val addedLectures = mutableSetOf<LectureTimeTable>()
    private val scraps = mutableListOf<LectureTimeTable>()
    var currentSelectedPosition = TIMETABLE_EMPTY_POSITION

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
                timetableLectureListener?.onCheckedChange(TIMETABLE_EMPTY_POSITION, item)
                currentSelectedPosition = TIMETABLE_EMPTY_POSITION
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

    inner class TimetableLectureViewHolder(val binding: ItemTimetableLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LectureTimeTable) {
            with(binding) {
                textViewLectureTitle.text = item.name
                textViewLectureCode.text = item.code
                textViewLectureStar.text = String.format("%1.1f", item.rating)
                textViewLectureProfessor.text = "${item.professor} (${item.classNumber})"
                textViewLectureCredit.text = context.getString(R.string.credit, item.designScore)
                textViewLectureGrade.text = context.getString(R.string.grade, item.grades)
                textViewLectureClassification.text = item.classification
                textViewLectureTime.text =
                    TimetableUtil.convertApiExpressionToKoreatechClassTime(context, item.classTime ?: "[]")
            }
        }

        fun setSelected(isSelected: Boolean) {
            binding.buttonAddLecture.setVisibility(!isSelected)
            binding.buttonRemoveLecture.setVisibility(isSelected)
        }

        fun setScrapButton(isSelected: Boolean) {
            binding.checkBoxScrap.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (isSelected) R.drawable.ic_check_v_selected
                    else R.drawable.ic_check_v_unselected
                )
            )
        }
    }
}