package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.databinding.ItemMyScrapLectureBinding
import `in`.hangang.hangang.util.diffutil.MyScrapDiffCallback
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.model.MutablePair

class MyScrapAdapter : OnItemClickRecyclerViewAdapter<MyScrapAdapter.ViewHolder>() {
    private val lectures = mutableListOf<Lecture>()
    private val selectedItemsPosition = SparseBooleanArray()
    var isEditMode = false
    set(value) {
        field = value
        updateLectures()
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
        holder.setSelection(selectedItemsPosition.get(position) && isEditMode)
    }

    override fun getItemCount() = lectures.size

    fun setLectures(lectures: List<Lecture>) {
        val diffCallback = MyScrapDiffCallback(this.lectures, lectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
        selectedItemsPosition.clear()
    }

    fun toggleLectureSelection(position: Int) {
        selectedItemsPosition.put(position, !selectedItemsPosition[position])

        notifyItemChanged(position)
    }

    fun selectAllLecture() {
        lectures.forEachIndexed {i, pair ->
            selectedItemsPosition.put(i, true)
        }
        updateLectures()
    }

    fun unselectAllLecture() {
        lectures.forEachIndexed {i, pair ->
            selectedItemsPosition.put(i, false)
        }
        updateLectures()
    }

    fun isSelectedAll() : Boolean {
        lectures.forEachIndexed {i, _ ->
            if(!selectedItemsPosition[i]) return false
        }
        return true
    }

    fun getSelectedLectures() : List<Lecture> {
        val selectedLecture = mutableListOf<Lecture>()
        lectures.forEachIndexed {i, lecture ->
            if(selectedItemsPosition[i]) selectedLecture.add(lecture)
        }
        return selectedLecture
    }

    private fun updateLectures() {
        setLectures(listOf(*lectures.toTypedArray()))
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