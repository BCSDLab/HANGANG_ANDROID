package `in`.hangang.core.view.spinner.adapter

import `in`.hangang.core.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HangangBottomSheetSpinnerAdapter :
    RecyclerView.Adapter<HangangBottomSheetSpinnerAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(value: CharSequence, position: Int)
    }

    var onItemClickListener : OnItemClickListener? = null

    var items : List<CharSequence> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.simple_text_item_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text1.text = items[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(items[position], position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val text1 : TextView = itemView.findViewById(android.R.id.text1)
    }

}