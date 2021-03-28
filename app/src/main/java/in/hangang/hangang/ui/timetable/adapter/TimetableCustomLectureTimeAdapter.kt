package `in`.hangang.hangang.ui.timetable.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.databinding.ItemTimetableCustomLectureTimestampBinding
import `in`.hangang.hangang.util.diffutil.CustomTimetableTimestampDiffCallback
import `in`.hangang.hangang.util.diffutil.LectureTimeTableDiffCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TimetableCustomLectureTimeAdapter(private val context: Context) :
    RecyclerView.Adapter<TimetableCustomLectureTimeAdapter.ViewHolder>() {

    val list = mutableListOf<CustomTimetableTimestamp>()
    val weekdaysText: Array<CharSequence> by lazy { context.resources.getTextArray(R.array.timetable_picker_weeks) }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    class ViewHolder(
        private val binding: ItemTimetableCustomLectureTimestampBinding,
        private val weekdaysText: Array<CharSequence>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customTimetableTimestamp: CustomTimetableTimestamp) {
            with(customTimetableTimestamp) {
                binding.textViewWeekday.text = weekdaysText[week]
                binding.textViewTime.text =
                    "${String.format("%02d", startTime.first)}:${
                        String.format(
                            "%02d",
                            startTime.second
                        )
                    } ~ ${String.format("%02d", endTime.first)}:${
                        String.format(
                            "%02d",
                            endTime.second
                        )
                    }"
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
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<CustomTimetableTimestamp>) {
        val diffCallback = CustomTimetableTimestampDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    inline fun setOnItemClickListener(crossinline listener: (view: View, position: Int) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                listener(view, position)
            }

        }
    }
}