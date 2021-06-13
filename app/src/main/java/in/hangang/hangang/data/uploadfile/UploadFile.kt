package `in`.hangang.hangang.data.uploadfile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadFile(
    val id: Int,
    @SerializedName("lecture_bank_id") val lectureBankId: Int,
    val url: String,
    val fileName: String,
    val ext: String,
    val size: Long
): Parcelable
