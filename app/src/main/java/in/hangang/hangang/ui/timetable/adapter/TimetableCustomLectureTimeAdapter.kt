package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.timetable.CustomTimetableTimestamp
import `in`.hangang.hangang.databinding.ItemTimetableCustomLectureTimestampBinding
import `in`.hangang.hangang.ui.timetable.listener.OnItemClickListener
import `in`.hangang.hangang.util.diffutil.CustomTimetableTimestampDiffCallback
import `in`.hangang.hangang.util.format
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableCustomLectureTimeAdapter(private val context: Context) :
        RecyclerView.Adapter<TimetableCustomLectureTimeAdapter.ViewHolder>() {

    private val list = mutableListOf<CustomTimetableTimestamp>()
    private val weekdaysText: Array<CharSequence> by lazy { context.resources.getTextArray(R.array.timetable_picker_weeks) }

    var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(
            val binding: ItemTimetableCustomLectureTimestampBinding,
            private val weekdaysText: Array<CharSequence>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customTimetableTimestamp: CustomTimetableTimestamp) {
            with(customTimetableTimestamp) {
                binding.textViewWeekday.text = weekdaysText[week]
                binding.textViewTime.text =
                        "${startTime.first.format(TIME_FORMAT)}:${
                            startTime.second.format(TIME_FORMAT)
                        } ~ ${endTime.first.format(TIME_FORMAT)}:${
                            endTime.second.format(TIME_FORMAT)
                        }"
                binding.textViewRemove.isVisible = list.size > 1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.bind(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_timetable_custom_lecture_timestamp, parent, false)
                )!!,
                weekdaysText
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val position = holder.absoluteAdapterPosition
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(holder.itemView, position)
        }
        holder.binding.textViewRemove.setOnClickListener {
            onItemClickListener?.onDeleteButtonClick(holder.binding.textViewRemove, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<CustomTimetableTimestamp>) {
        this.list.clear()
        this.list.addAll(newList)

        notifyDataSetChanged()
    }

    inline fun setOnItemClickListener(
            crossinline onItemClick: (view: View, position: Int) -> Unit,
            crossinline onDeleteButtonClick: (view: View, position: Int) -> Unit,
    ) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                onItemClick(view, position)
            }

            override fun onDeleteButtonClick(view: View, position: Int) {
                onDeleteButtonClick(view, position)
            }
        }
    }

    companion object {
        const val TIME_FORMAT = "%02d"
    }
}