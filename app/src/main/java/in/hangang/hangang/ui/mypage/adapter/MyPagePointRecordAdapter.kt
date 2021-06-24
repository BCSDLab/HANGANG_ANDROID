package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.PointRecord
import `in`.hangang.hangang.databinding.ItemMyPagePointRecordBinding
import `in`.hangang.hangang.util.DateUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class MyPagePointRecordAdapter : RecyclerView.Adapter<MyPagePointRecordAdapter.ViewHolder>() {

    private val pointRecords = mutableListOf<PointRecord>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_my_page_point_record, parent, false)
            )!!
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pointRecord = pointRecords[position])
    }

    override fun getItemCount() = pointRecords.size

    fun setPointRecords(pointRecords: List<PointRecord>) {
        this.pointRecords.clear()
        this.pointRecords.addAll(pointRecords)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemMyPagePointRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pointRecord: PointRecord) {
            binding.pointRecord = pointRecord
            binding.textViewPointRecordTime.text = DateUtil.formatApiStringDateTime(pointRecord.createdAt, DateUtil.MY_PAGE_POINT_RECORD_DATE_TIME)
            binding.executePendingBindings()
        }
    }
}