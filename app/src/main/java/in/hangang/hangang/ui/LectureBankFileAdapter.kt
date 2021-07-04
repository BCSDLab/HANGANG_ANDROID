package `in`.hangang.hangang.ui

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.databinding.ItemLectureBankUploadFileBinding
import `in`.hangang.hangang.util.LectureBankUtil
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankFileAdapter : OnItemClickRecyclerViewAdapter<LectureBankFileAdapter.ViewHolder>() {

    private val files = mutableListOf<UploadFile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.bind(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_lecture_bank_upload_file, parent, false)
            )!!,
            parent.context
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

    inner class ViewHolder(private val binding: ItemLectureBankUploadFileBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uploadFile: UploadFile) {
            binding.imageViewFileType.setImageDrawable(
                LectureBankUtil.getLectureBankFileTypeImage(
                    context,
                    uploadFile.ext
                )
            )
            binding.textViewFileName.text = uploadFile.fileName
        }
    }
}