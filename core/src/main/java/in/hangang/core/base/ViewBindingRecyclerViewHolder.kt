package `in`.hangang.core.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewBindingRecyclerViewHolder<T : ViewDataBinding>(
        val binding: T
) : RecyclerView.ViewHolder(
        binding.root
)