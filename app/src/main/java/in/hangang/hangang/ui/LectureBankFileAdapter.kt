package `in`.hangang.hangang.ui

import `in`.hangang.core.view.recyclerview.OnItemClickRecyclerViewAdapter
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.uploadfile.UploadFile
import `in`.hangang.hangang.databinding.ItemLectureBankUploadFileBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class LectureBankFileAdapter : RecyclerView.Adapter<LectureBankFileAdapter.ViewHolder>() {

    private val files = mutableListOf<UploadFile>()
    private val downloadStatusMap : MutableMap<UploadFile, Pair<Int, Int>> = mutableMapOf()

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

    var onItemClickListener : OnItemClickListener? = null

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
        holder.bind(files[position])
        downloadStatusMap[files[position]]?.let {
            holder.setProgressBar(it.first, it.second)
        } ?: holder.setProgressBar(-2, -2)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(
                position, files[position], downloadStatusMap[files[position]]?.second ?: -2 > -2
            )
        }
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

    fun setDownloadStatus(position: Int, progress: Int, max: Int) {
        val beforeProgress = downloadStatusMap[files[position]]?.first ?: -1
        val beforeMax = downloadStatusMap[files[position]]?.second ?: -2
        downloadStatusMap[files[position]] = progress to max
        if(if(beforeMax < 0) {
            beforeMax != max
        } else {
            beforeMax != max ||
            beforeProgress != progress
        })
            notifyItemChanged(position)
    }

    fun setDownloadStatus(uploadFile: UploadFile, progress: Int, max: Int) {
        setDownloadStatus(findUploadFilePosition(uploadFile), progress, max)
    }

    fun findUploadFilePosition(uploadFile: UploadFile) : Int {
        return files.indexOf(uploadFile)
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

        fun setProgressBar(progress: Int, max: Int) {
            when {
                max < PROGRESS_INDETERMINATE -> binding.layoutDownloading.visibility = View.GONE
                max == PROGRESS_INDETERMINATE -> {
                    binding.layoutDownloading.visibility = View.VISIBLE
                    binding.progressBarDownload.isIndeterminate = true
                }
                else -> {
                    binding.layoutDownloading.visibility = View.VISIBLE
                    binding.progressBarDownload.max = max
                    binding.progressBarDownload.progress = progress
                    binding.progressBarDownload.isIndeterminate = false
                }
            }
        }
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Int, UploadFile, Boolean) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, uploadFile: UploadFile, isDownloading: Boolean) {
                onItemClick(position, uploadFile, isDownloading)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, uploadFile: UploadFile, isDownloading: Boolean)
    }

    companion object {
        const val PROGRESS_INDETERMINATE = -1
    }

}