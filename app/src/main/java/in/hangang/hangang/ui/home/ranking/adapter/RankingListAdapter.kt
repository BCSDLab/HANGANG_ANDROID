package `in`.hangang.hangang.ui.home.ranking.adapter

import `in`.hangang.core.base.ViewBindingRecyclerViewHolder
import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ItemHomeRankingListBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

class RankingListAdapter : OnItemClickRecyclerViewAdapter<RankingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_home_ranking_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rankingItemNumber.text = "${position + 1}"
        holder.binding.rankingItemStarNumber.text = "4.2"
        holder.binding.rankingItemTitle.text = "사랑의 역사"
        holder.binding.rankingItemProfessor.text = "김사랑"

        if (position == itemCount - 1) holder.binding.divider.visibility = View.GONE
    }

    override fun getItemCount(): Int = 5

    class ViewHolder(itemHomeRankingListBinding: ItemHomeRankingListBinding) :
        ViewBindingRecyclerViewHolder<ItemHomeRankingListBinding>(itemHomeRankingListBinding)
}