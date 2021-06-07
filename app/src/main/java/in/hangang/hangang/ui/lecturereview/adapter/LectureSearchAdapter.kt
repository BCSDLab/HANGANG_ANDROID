package `in`.hangang.hangang.ui.lecturereview.adapter

import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.databinding.ItemLectureSearchBinding
import `in`.hangang.hangang.util.LogUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LectureSearchAdapter : ListAdapter<String, LectureSearchAdapter.ViewHolder>(
    lectureSearchDiffUtil){
    private lateinit var queryClickListener: RecyclerViewClickListener
    private lateinit var deleteClickListener: RecyclerViewClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLectureSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val query = getItem(position)
        if(query != null){
            holder.bind(query)
        }else{
            LogUtil.e("error")
        }
        holder.binding.lecrueSearchDeleteButton.setOnClickListener { v->
            deleteClickListener.onClick(v, position, query)
        }
        holder.binding.lectureSearchQuery.setOnClickListener { v->
            queryClickListener.onClick(v, position, query)
        }
    }
    fun setClickQueryRecyclerViewListener(recyclerViewClickListener: RecyclerViewClickListener){
        this.queryClickListener = recyclerViewClickListener
    }
    fun setDeleteRecyclerViewListener(recyclerViewClickListener: RecyclerViewClickListener){
        this.deleteClickListener = recyclerViewClickListener
    }


    class ViewHolder(val binding: ItemLectureSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String){
            binding.apply {
                query = item
                executePendingBindings()
            }
        }
    }
    companion object {
        val lectureSearchDiffUtil = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}