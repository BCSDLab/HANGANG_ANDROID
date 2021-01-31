package `in`.hangang.hangang.ui.home.recommendedlectures.adapter

import `in`.hangang.core.base.ViewBindingRecyclerViewHolder
import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ItemHomeRecommendedLecturesBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

class RecommendedLecturesRecyclerViewAdapter() :
    OnItemClickRecyclerViewAdapter<RecommendedLecturesRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHomeRecommendedLecturesBinding) :
        ViewBindingRecyclerViewHolder<ItemHomeRecommendedLecturesBinding>(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_home_recommended_lectures,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            imageViewRecommendedLectures.setImageResource(R.drawable.hangang_logo_small)
            textViewRecommendedLectures.text = "문명과 멸망"
            extraLeftPadding.visibility = if (position == 0) View.VISIBLE else View.GONE
            extraRightPadding.visibility =
                    if (position == itemCount - 1) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = 8
}