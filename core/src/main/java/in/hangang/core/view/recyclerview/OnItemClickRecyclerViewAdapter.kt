package `in`.hangang.core.view.recyclerview

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

abstract class OnItemClickRecyclerViewAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var onItemClickListener: OnItemClickListener? = null
    private var recyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(recyclerView, holder.itemView, position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(parent: RecyclerView?, view: View?, position: Int)
    }
}

