package `in`.hangang.core.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewBindingRecyclerViewHolder<T : ViewDataBinding>(
    viewDataBinding: T
) : RecyclerView.ViewHolder(
    viewDataBinding.root
) {

}