package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.ItemTimetableTimetableTextBinding
import `in`.hangang.hangang.util.diffutil.TimeTableDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableTimetablesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<Pair<Int, Any?>>()

    companion object {
        const val TYPE_SEMESTER = 0
        const val TYPE_TIMETABLE = 1
        const val TYPE_DIVIDER = 2

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_SEMESTER -> SemesterViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_timetable_semester_text, parent, false)
            )
            TYPE_TIMETABLE -> TimetableViewHolder(
                DataBindingUtil.bind(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_timetable_timetable_text, parent, false)
                )!!
            )
            TYPE_DIVIDER -> DividerViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_timetable_divider, parent, false)
            )
            else -> throw IllegalStateException("Wrong viewType!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when(item.first) {
            TYPE_SEMESTER -> ((holder as SemesterViewHolder).itemView as TextView).text = item.second as String
            TYPE_TIMETABLE -> {
                (holder as TimetableViewHolder).bind(item.second as TimeTable)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].first
    }

    fun updateItem(timetables: List<TimeTable>) {
        val newList = makeList(timetables)
        val diffCallback = TimeTableDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    private fun makeList(lectures: List<TimeTable>) : List<Pair<Int, Any?>> {
        var lastSemesterDateId = 0
        val newList = mutableListOf<Pair<Int, Any?>>()
        lectures.sortedBy { it.semesterDateId }.forEach {
            if(lastSemesterDateId != it.semesterDateId) {
                lastSemesterDateId = it.semesterDateId
                if(newList.isNotEmpty()) newList.add(Pair(TYPE_DIVIDER, null))
                newList.add(Pair(TYPE_SEMESTER, it.semesterDateId.toString())) //TODO : 학기 id를 실제 학기 문자열로 변환하는 과정 필요
            }
            newList.add(Pair(TYPE_TIMETABLE, it))
        }
        return newList
    }

    inner class SemesterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class TimetableViewHolder(private val binding : ItemTimetableTimetableTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : TimeTable) {
            binding.timetable = item
            binding.executePendingBindings()
        }
    }
    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}