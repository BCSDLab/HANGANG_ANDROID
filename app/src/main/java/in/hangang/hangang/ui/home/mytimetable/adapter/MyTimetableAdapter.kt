package `in`.hangang.hangang.ui.home.mytimetable.adapter

import `in`.hangang.core.base.ViewBindingRecyclerViewHolder
import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ItemHomeMyTimetableListBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

class MyTimetableAdapter : OnItemClickRecyclerViewAdapter<MyTimetableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_home_my_timetable_list,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemMyTimetableTitle.text = "사랑의 역사"
        holder.binding.itemMyTimetableProfessor.text = "김사랑"

        if(position == itemCount - 1) holder.binding.divider.visibility = View.GONE
    }

    override fun getItemCount(): Int = 8

    class ViewHolder(itemHomeMyTimetableListBinding: ItemHomeMyTimetableListBinding) :
        ViewBindingRecyclerViewHolder<ItemHomeMyTimetableListBinding>(itemHomeMyTimetableListBinding)
}