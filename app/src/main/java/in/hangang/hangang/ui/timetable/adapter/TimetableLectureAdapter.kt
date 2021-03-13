package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.ui.timetable.adapter.viewholder.TimetableLectureViewHolder
import `in`.hangang.hangang.util.diffutil.LectureDiffCallback
import `in`.hangang.hangang.util.withThread
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Single

class TimetableLectureAdapter : RecyclerView.Adapter<TimetableLectureViewHolder>() {

    private val lectures = mutableListOf<Lecture>()

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

    fun updateItem(lectures: List<Lecture>) {
        val diffCallback = LectureDiffCallback(this.lectures, lectures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.lectures.clear()
        this.lectures.addAll(lectures)

        diffResult.dispatchUpdatesTo(this)
        println()
    }
}