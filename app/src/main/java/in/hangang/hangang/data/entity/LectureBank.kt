package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class LectureBank(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val title: String?,
    val reported: Boolean,
    val lecture: SimpleLecture,
    val uploadFiles: List<UploadFile>
)