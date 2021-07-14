package `in`.hangang.hangang.ui.mypage.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.databinding.ItemMyPagePurchasedBankBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyPagePurchasedBankAdapter : RecyclerView.Adapter<MyPagePurchasedBankAdapter.ViewHolder>() {

    private val lectureBanks = mutableListOf<LectureBank>()

    var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_my_page_purchased_bank, parent, false)
            )!!,
            LinearLayoutManager(parent.context, RecyclerView.HORIZONTAL, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lectureBanks[holder.absoluteAdapterPosition])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(holder.absoluteAdapterPosition, lectureBanks[holder.absoluteAdapterPosition])
        }
    }

    override fun getItemCount() = lectureBanks.size

    fun setLectureBanks(lectureBanks: List<LectureBank>) {
        this.lectureBanks.clear()
        this.lectureBanks.addAll(lectureBanks)
        notifyDataSetChanged()
    }

    inline fun setOnItemClickListener(
        crossinline onItemClick : (Int, LectureBank) -> Unit,
        crossinline onFileItemClick: (Int, LectureBank, UploadFile, Boolean) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, lectureBank: LectureBank) {
                onItemClick(position, lectureBank)
            }

            override fun onFileItemClick(position: Int, lectureBank: LectureBank, uploadFile: UploadFile, isDownloading: Boolean) {
                onFileItemClick(position, lectureBank, uploadFile, isDownloading)
            }
        }
    }

    inner class ViewHolder(
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
                    isEnabled = true
                    setOnItemClickListener { i, uploadFile, b ->
                        this@MyPagePurchasedBankAdapter.onItemClickListener?.onFileItemClick(i, lectureBank, uploadFile, b)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, lectureBank: LectureBank)
        fun onFileItemClick(position: Int, lectureBank: LectureBank, uploadFile: UploadFile, isDownloading: Boolean)
    }
}