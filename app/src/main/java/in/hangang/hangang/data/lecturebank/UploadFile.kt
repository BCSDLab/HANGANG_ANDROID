package `in`.hangang.hangang.data.lecturebank

import com.google.gson.annotations.SerializedName

data class UploadFile(
    val id: Int,
    @SerializedName("lecture_bank_id") val lectureBankId: Int,
    val url: String,
    val fileName: String,
    val ext: String,
    val size: Long
)
