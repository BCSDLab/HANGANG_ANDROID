package `in`.hangang.hangang.ui

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.lecturebank.UploadFile
import `in`.hangang.hangang.databinding.ItemLectureBankUploadFileBinding
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankFileAdapter() : OnItemClickRecyclerViewAdapter<LectureBankFileAdapter.ViewHolder>() {

    private val files = mutableListOf<UploadFile>()

    private val fileExtMap : Map<String, Pair<Int, Int>> by lazy { mapOf(
        "cell" to Pair(R.drawable.ic_cell_disabled, R.drawable.ic_cell),
        "doc" to Pair(R.drawable.ic_doc_disabled, R.drawable.ic_doc),
        "hwp" to Pair(R.drawable.ic_hwp_disabled, R.drawable.ic_hwp),
        "jpg" to Pair(R.drawable.ic_jpg_disabled, R.drawable.ic_jpg),
        "pdf" to Pair(R.drawable.ic_pdf_disabled, R.drawable.ic_pdf),
        "png" to Pair(R.drawable.ic_png_disabled, R.drawable.ic_png),
        "ppt" to Pair(R.drawable.ic_ppt_disabled, R.drawable.ic_ppt),
        "show" to Pair(R.drawable.ic_show_disabled, R.drawable.ic_show),
        "txt" to Pair(R.drawable.ic_txt_disabled, R.drawable.ic_txt),
        "xls" to Pair(R.drawable.ic_xls_disabled, R.drawable.ic_xls),
        "zip" to Pair(R.drawable.ic_zip_disabled, R.drawable.ic_zip),
    )}

    var isEnabled = false
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_lecture_bank_upload_file, parent, false)
            )!!
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(files[position])
    }

    override fun getItemCount() = files.size

    fun setFiles(files: List<UploadFile>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    fun getUploadFile(position: Int): UploadFile {
        return files[position]
    }

    inner class ViewHolder(private val binding: ItemLectureBankUploadFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uploadFile: UploadFile) {
            fileExtMap[uploadFile.ext]?.let {
                binding.imageViewFileType.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    if(!isEnabled) it.first
                    else it.second
                ))
            }

            binding.textViewFileName.text = uploadFile.fileName
        }
    }


}