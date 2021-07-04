package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.databinding.ItemMyPagePurchasedBankBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyPagePurchasedBankAdapter : OnItemClickRecyclerViewAdapter<MyPagePurchasedBankAdapter.ViewHolder>() {

    private val lectureBanks = mutableListOf<LectureBank>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_my_page_purchased_bank, parent, false)
            )!!,
            LinearLayoutManager(parent.context, RecyclerView.HORIZONTAL, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lectureBanks[holder.adapterPosition])
    }

    override fun getItemCount() = lectureBanks.size

    fun setLectureBanks(lectureBanks: List<LectureBank>) {
        this.lectureBanks.clear()
        this.lectureBanks.addAll(lectureBanks)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemMyPagePurchasedBankBinding,
        private val layoutManager: RecyclerView.LayoutManager
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.recyclerViewFile.layoutManager = layoutManager
        }

        fun bind(lectureBank: LectureBank) {
            with(binding) {
                textViewBankTitle.text = lectureBank.title
                textViewLectureLectureName.text = lectureBank.lecture.name
                textViewLectureLectureProfessor.text = lectureBank.lecture.professor
                recyclerViewFile.adapter = LectureBankFileAdapter().apply {
                    setFiles(lectureBank.uploadFiles ?: listOf())
                }
            }
        }
    }
}