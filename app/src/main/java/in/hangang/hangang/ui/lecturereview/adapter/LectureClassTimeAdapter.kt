package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.ItemLectureClassTimeBinding
import `in`.hangang.hangang.util.ClassLectureTimeUtil
import `in`.hangang.hangang.util.LogUtil
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LectureClassTimeAdapter(private val context: Context) :
    ListAdapter<ClassLecture, LectureClassTimeAdapter.ViewHolder>(classLectureDiffUtil) {
    private lateinit var classClickListener: RecyclerViewClickListener
    private var checkList = ArrayList<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLectureClassTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    fun setClassClickListener(recyclerViewClickListener: RecyclerViewClickListener){
        this.classClickListener = recyclerViewClickListener
    }
    fun setCheckList(list: ArrayList<Boolean>){
        checkList.clear()
        checkList.addAll(list)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classLectureItem = getItem(position)
        if (classLectureItem != null) {
            holder.bind(classLectureItem)
            if(!checkList.isEmpty()) {
                if (checkList[position]) {
                    holder.binding.lectureDetailClassAddButton.background =
                        context.getDrawable(R.drawable.rectangle_rounded_corner_mango)
                    holder.binding.lectureDetailClassAddButton.text = "빼기"
                } else {
                    holder.binding.lectureDetailClassAddButton.background =
                        context.getDrawable(R.drawable.rectangle_rounded_corner_blue_200)
                    holder.binding.lectureDetailClassAddButton.text = "담기"
                }
            }
            holder.binding.lectureDetailClassAddButton.setOnClickListener{ v ->
                if(classClickListener != null){
                    classClickListener.onClick(v, position, classLectureItem)
                }
            }
        } else {
            LogUtil.e("error")
        }
    }

    class ViewHolder(val binding: ItemLectureClassTimeBinding) :
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
                return oldItem.id == newItem.id
            }
        }
    }

}