package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.entity.lecturebank.toLectureBank
import `in`.hangang.hangang.databinding.ItemLectureBankBinding
import `in`.hangang.hangang.util.diffutil.MyScrapLectureBankDiffCallback
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyScrapLectureBankAdapter : RecyclerView.Adapter<MyScrapLectureBankAdapter.ViewHolder>() {
    private val scraps = mutableListOf<LectureBankScrap>()
    private val selectedLectureBanks = SparseBooleanArray()

    var onItemClickListener : OnItemClickListener? = null

    var isEditMode = false
        set(value) {
            field = value
            scraps.forEachIndexed { i, lecture ->
                notifyItemChanged(i, selectedLectureBanks[i])
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_lecture_bank, parent, false)
            )!!,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val position = holder.absoluteAdapterPosition
        holder.bind(scraps[position].toLectureBank())
        holder.setSelection(selectedLectureBanks[position] && isEditMode)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, scraps[position])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            holder.setSelection((payloads[0] as? Boolean ?: false) && isEditMode)
        }
    }

    override fun getItemCount() = scraps.size

    fun setLectureBanks(scraps: List<LectureBankScrap>) {
        val diffCallback = MyScrapLectureBankDiffCallback(this.scraps, scraps)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.scraps.clear()
        this.scraps.addAll(scraps)

        diffResult.dispatchUpdatesTo(this)
    }

    fun toggleLectureSelection(position: Int) {
        selectedLectureBanks.put(position, !selectedLectureBanks[position])

        notifyItemChanged(position, selectedLectureBanks[position])
    }

    fun selectAllLecture() {
        scraps.forEachIndexed { i, pair ->
            selectedLectureBanks.put(i, true)
        }
        notifyItemRangeChanged(0, itemCount, true)
    }

    fun unselectAllLecture() {
        scraps.forEachIndexed { i, pair ->
            selectedLectureBanks.put(i, false)
        }
        notifyItemRangeChanged(0, itemCount, false)
    }

    fun isSelectedAll(): Boolean {
        scraps.forEachIndexed { i, _ ->
            if (!selectedLectureBanks[i]) return false
        }
        return true
    }

    fun isLeastOneSelected(): Boolean {
        scraps.forEachIndexed { i, _ ->
            if (selectedLectureBanks[i]) return true
        }
        return false
    }

    fun getSelectedLectures(): List<LectureBankScrap> {
        val selectedLectureBank = mutableListOf<LectureBankScrap>()
        scraps.forEachIndexed { i, scrap ->
            if (selectedLectureBanks[i]) selectedLectureBank.add(scrap)
        }
        return selectedLectureBank
    }

    inline fun setOnItemClickListener(crossinline onItemClick : (Int, LectureBankScrap) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, lectureBankScrap: LectureBankScrap) {
                onItemClick(position, lectureBankScrap)
            }
        }
    }

    class ViewHolder(private val binding: ItemLectureBankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lectureBank: LectureBank) {
            with(binding) {
                bank = lectureBank
                lectureBankScrap.isVisible = true
                Glide.with(binding.root).load(lectureBank.thumbnail).into(binding.imageViewLectureBankThumbnail)
                executePendingBindings()
            }
        }

        fun setSelection(isSelected: Boolean) {
            binding.root.isSelected = isSelected
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, lectureBankScrap: LectureBankScrap)
    }
}