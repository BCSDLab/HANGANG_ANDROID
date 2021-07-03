package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.databinding.ItemMyScrapLectureBinding
import `in`.hangang.hangang.util.diffutil.MyScrapLectureReviewDiffCallback
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MyScrapLectureReviewAdapter : OnItemClickRecyclerViewAdapter<MyScrapLectureReviewAdapter.ViewHolder>() {
    private val lectures = mutableListOf<Lecture>()
    private val selectedLectures = SparseBooleanArray()
    var isEditMode = false
        set(value) {
            field = value
            lectures.forEachIndexed { i, lecture ->
                notifyItemChanged(i, selectedLectures[i])
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_my_scrap_lecture, parent, false)
            )!!,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(lectures[position])
        holder.setSelection(selectedLectures[position] && isEditMode)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            holder.setSelection((payloads[0] as? Boolean ?: false) && isEditMode)
        }
    }

    override fun getItemCount() = lectures.size

    fun setLectures(lectures: List<Lecture>) {
        val diffCallback = MyScrapLectureReviewDiffCallback(this.lectures, lectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
    }

    fun toggleLectureSelection(position: Int) {
        selectedLectures.put(position, !selectedLectures[position])

        notifyItemChanged(position, selectedLectures[position])
    }

    fun selectAllLecture() {
        lectures.forEachIndexed { i, pair ->
            selectedLectures.put(i, true)
        }
        notifyItemRangeChanged(0, itemCount, true)
    }

    fun unselectAllLecture() {
        lectures.forEachIndexed { i, pair ->
            selectedLectures.put(i, false)
        }
        notifyItemRangeChanged(0, itemCount, false)
    }

    fun isSelectedAll(): Boolean {
        lectures.forEachIndexed { i, _ ->
            if (!selectedLectures[i]) return false
        }
        return true
    }

    fun isLeastOneSelected(): Boolean {
        lectures.forEachIndexed { i, _ ->
            if (selectedLectures[i]) return true
        }
        return false
    }

    fun getSelectedLectures(): List<Lecture> {
        val selectedLecture = mutableListOf<Lecture>()
        lectures.forEachIndexed { i, lecture ->
            if (selectedLectures[i]) selectedLecture.add(lecture)
        }
        return selectedLecture
    }

    class ViewHolder(private val binding: ItemMyScrapLectureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lecture: Lecture) {
            with(binding) {
                this.lecture = lecture
                val top3HashTag = lecture.top3HashTag
                top3HashTag.getOrNull(0)?.let {
                    textViewLectureHashtag1.text = "# ${it.tag}"
                }
                top3HashTag.getOrNull(1)?.let {
                    textViewLectureHashtag2.text = "# ${it.tag}"
                }
                top3HashTag.getOrNull(2)?.let {
                    textViewLectureHashtag3.text = "# ${it.tag}"
                }
                //executePendingBindings()
            }
        }

        fun setSelection(isSelected: Boolean) {
            binding.root.isSelected = isSelected
        }
    }
}