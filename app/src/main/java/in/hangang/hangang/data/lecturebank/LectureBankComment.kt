package `in`.hangang.hangang.data.lecturebank

import com.google.gson.annotations.SerializedName

data class LectureBankComment(
    val id: Int,
    @SerializedName("lecture_bank_id") val lectureBankId: Int,
    @SerializedName("user_id") val userId: Int,
    val nickname: String,
    val comments: String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String,
)
