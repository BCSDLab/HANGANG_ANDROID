package `in`.hangang.hangang.ui.home.recentlectures.adapter

import `in`.hangang.core.base.ViewBindingRecyclerViewHolder
import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ItemHomeRecentLecturesListBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

class RecentLecturesRecyclerViewAdapter : OnItemClickRecyclerViewAdapter<RecentLecturesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_home_recent_lectures_list,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recentLecturesItemTitle.text = "사랑의 역사"
        holder.binding.recentLecturesItemProfessor.text = "김사랑"
        holder.binding.recentLecturesItemStarNumber.text = "4.2"

        if(position == itemCount - 1) holder.binding.divider.visibility = View.GONE
    }

    override fun getItemCount(): Int = 0

    class ViewHolder(itemHomeRecentLecturesListBinding: ItemHomeRecentLecturesListBinding) :
        ViewBindingRecyclerViewHolder<ItemHomeRecentLecturesListBinding>(itemHomeRecentLecturesListBinding)
}