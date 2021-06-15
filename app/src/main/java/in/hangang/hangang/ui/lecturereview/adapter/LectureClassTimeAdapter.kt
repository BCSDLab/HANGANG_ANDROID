package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.hangang.data.entity.evaluation.ClassLecture
import `in`.hangang.hangang.databinding.ItemLectureClassTimeBinding
import `in`.hangang.hangang.util.ClassLectureTimeUtil
import `in`.hangang.hangang.util.LogUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LectureClassTimeAdapter :
    ListAdapter<ClassLecture, LectureClassTimeAdapter.ViewHolder>(classLectureDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLectureClassTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classLectureItem = getItem(position)
        if (classLectureItem != null) {
            holder.bind(classLectureItem)
        } else {
            LogUtil.e("error")
        }
    }

    class ViewHolder(private val binding: ItemLectureClassTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClassLecture) {
            binding.apply {
                lectureClassItem = item
                var timeList = item.getClassList(item.classTime)
                if (timeList.size != 0) {
                    var text = ClassLectureTimeUtil.getClassLectureTime(
                        timeList[0],
                        timeList[0],
                        timeList[timeList.size - 1]
                    )
                    LogUtil.e(text)
                    lectureDetailClassTimeTextview.text = text
                }
                executePendingBindings()
            }
        }
    }

    companion object {
        val classLectureDiffUtil = object : DiffUtil.ItemCallback<ClassLecture>() {
            override fun areItemsTheSame(
                oldItem: ClassLecture,
                newItem: ClassLecture
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ClassLecture,
                newItem: ClassLecture
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}