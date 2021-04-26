package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.core.view.setVisibility
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

    private val list = mutableListOf<Any>()

    var timetableListRecyclerViewOnItemClickListener: TimetableListRecyclerViewOnItemClickListener? = null

    var mainTimeTableId: Int = 0
        set(value) {
            list.forEachIndexed { i, it ->
                if (it is TimeTable && it.id == field) {
                    notifyItemChanged(i)
                    return@forEachIndexed
                }
            }
            field = value
        }

    var selectedTimeTableId: Int = 0
        set(value) {
            list.forEachIndexed { i, it ->
                if (it is TimeTable && it.id == field) {
                    notifyItemChanged(i)
                    return@forEachIndexed
                }
            }
            field = value
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
            else -> throw IllegalStateException("Wrong viewType!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (item) {
            is Int -> {
                with(holder as? SemesterViewHolder) {
                    this?.showDivider(position != 0)
                    this?.textView?.apply {
                        text = SemesterUtil.getSemesterText(context, item)
                        setOnClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onSemesterItemClick(item, text.toString())
                        }
                        setOnLongClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onSemesterItemLongClick(item, text.toString())
                            false
                        }
                    }
                }
            }
            is TimeTable -> {
                with(holder as TimetableViewHolder) {
                    bind(item)
                    setTimeTableStyle(item.id == mainTimeTableId, item.id == selectedTimeTableId)
                    itemView.apply {
                        setOnClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onTimeTableItemClick(item)
                        }
                        setOnLongClickListener {
                            timetableListRecyclerViewOnItemClickListener?.onTimeTableItemLongClick(item)
                            false
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Int -> TYPE_SEMESTER
            is TimeTable -> TYPE_TIMETABLE
            else -> TYPE_INVALID
        }
    }

    fun updateItem(timetables: Map<Int, List<TimeTable>>) {
        updateAdapterItem(timetables.flatMap {
            mutableListOf<Any>(it.key).apply { addAll(it.value) }
        })
    }

    private fun updateAdapterItem(timetables: List<Any>) {
        val diffCallback = TimeTableDiffCallback(list, timetables)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(timetables)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class SemesterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val divider: View by lazy { itemView.findViewById(R.id.divider) }
        val textView: TextView by lazy { itemView.findViewById(R.id.text_view) }

        fun showDivider(show: Boolean) {
            divider.setVisibility(show)
        }
    }

    inner class TimetableViewHolder(private val binding: ItemTimetableTimetableTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeTable) {
            binding.timetable = item
            binding.executePendingBindings()
        }

        fun setTimeTableStyle(isMainTimeTable: Boolean, isSelectedTimeTable: Boolean) {
            binding.textView.isEnabled = isSelectedTimeTable
            binding.badge.setVisibility(isMainTimeTable)
        }
    }

    companion object {
        const val TYPE_SEMESTER = 0
        const val TYPE_TIMETABLE = 1
        const val TYPE_INVALID = -1
    }
}