package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.core.view.dp2Px
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.ItemTimetableTimetableTextBinding
import `in`.hangang.hangang.ui.timetable.listener.TimetableListRecyclerViewOnItemClickListener
import `in`.hangang.hangang.util.SemesterUtil
import `in`.hangang.hangang.util.diffutil.TimeTableDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableTimetablesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SEMESTER = 0
        const val TYPE_TIMETABLE = 1
        const val TYPE_DIVIDER = 2
    }

    private val list = mutableListOf<Pair<Int, Any?>>()

    var timetableListRecyclerViewOnItemClickListener: TimetableListRecyclerViewOnItemClickListener? = null

    var mainTimeTableId: Int = 0
        set(value) {
            val before = field
            field = value
            list.forEachIndexed { index, pair ->
                if ((pair.second as? TimeTable)?.id == value || (pair.second as? TimeTable)?.id == before)
                    notifyItemChanged(index)
            }
        }

    var selectedTimeTableId: Int = 0
        set(value) {
            val before = field
            field = value
            list.forEachIndexed { index, pair ->
                if ((pair.second as? TimeTable)?.id == value || (pair.second as? TimeTable)?.id == before)
                    notifyItemChanged(index)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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
        if (item === list.last()) with(holder.itemView) {
            setPadding(paddingLeft, paddingTop, paddingRight, dp2Px(16f))
        }
        when (item.first) {
            TYPE_SEMESTER -> ((holder as SemesterViewHolder).itemView as TextView).apply {
                text = SemesterUtil.getSemesterText(context, item.second as Int)
                setOnClickListener {
                    timetableListRecyclerViewOnItemClickListener?.onSemesterItemClick(item.second as Int, text.toString())
                }
                setOnLongClickListener {
                    timetableListRecyclerViewOnItemClickListener?.onSemesterItemLongClick(item.second as Int, text.toString())
                    false
                }
            }
            TYPE_TIMETABLE -> {
                with(holder as TimetableViewHolder) {
                    with(item.second as TimeTable) {
                        bind(this)
                        setTimeTableStyle(this.id == mainTimeTableId, this.id == selectedTimeTableId)
                    }
                    itemView.apply {
                        setOnClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onTimeTableItemClick(item.second as TimeTable)
                        }
                        setOnLongClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onTimeTableItemLongClick(item.second as TimeTable)
                            false
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].first
    }

    fun updateItem(timetables: List<TimeTable>) {
        updateAdapterItem(makeList(timetables))
    }

    private fun updateAdapterItem(timetables: List<Pair<Int, Any?>>) {
        val diffCallback = TimeTableDiffCallback(list, timetables)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(timetables)

        diffResult.dispatchUpdatesTo(this)
    }

    private fun makeList(lectures: List<TimeTable>): List<Pair<Int, Any?>> {
        var lastSemesterDateId = 0
        val newList = mutableListOf<Pair<Int, Any?>>()
        lectures.sortedByDescending { it.semesterDateId }.forEach {
            if (lastSemesterDateId != it.semesterDateId) {
                lastSemesterDateId = it.semesterDateId
                if (newList.isNotEmpty()) newList.add(Pair(TYPE_DIVIDER, null))
                newList.add(Pair(TYPE_SEMESTER, it.semesterDateId))
            }
            newList.add(Pair(TYPE_TIMETABLE, it))
        }
        return newList
    }

    inner class SemesterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class TimetableViewHolder(private val binding: ItemTimetableTimetableTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeTable) {
            binding.timetable = item
            binding.executePendingBindings()
        }

        fun setTimeTableStyle(isMainTimeTable: Boolean, isSelectedTimeTable: Boolean) {
            binding.textView.isEnabled = isSelectedTimeTable
            binding.badge.visibility = visibleGone(isMainTimeTable)
        }
    }

    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}