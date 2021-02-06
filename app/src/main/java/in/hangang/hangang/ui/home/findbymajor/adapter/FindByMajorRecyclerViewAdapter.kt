package `in`.hangang.hangang.ui.home.findbymajor.adapter

import `in`.hangang.core.base.ViewBindingRecyclerViewHolder
import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ItemHomeFindByMajorBinding
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

class FindByMajorRecyclerViewAdapter(val context: Context) :
        OnItemClickRecyclerViewAdapter<FindByMajorRecyclerViewAdapter.ViewHolder>() {

    private val fullMajors: Array<String> by lazy { context.resources.getStringArray(R.array.major_full_word_wrap) }
    private val coverDrawables: TypedArray by lazy {
        context.resources.obtainTypedArray(R.array.find_by_major_cover_drawables)
    }

    class ViewHolder(binding: ItemHomeFindByMajorBinding) :
            ViewBindingRecyclerViewHolder<ItemHomeFindByMajorBinding>(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_home_find_by_major,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            imageViewFindByMajor.setImageResource(coverDrawables.getResourceId(position, 0))
            textViewFindByMajor.text = fullMajors[position]
            extraLeftPadding.visibility = if (position == 0) View.VISIBLE else View.GONE
            extraRightPadding.visibility =
                    if (position == itemCount - 1) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = fullMajors.size
}