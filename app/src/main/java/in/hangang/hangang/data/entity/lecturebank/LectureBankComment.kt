package `in`.hangang.hangang.data.entity.lecturebank

import com.google.gson.annotations.SerializedName

data class LectureBankComment(
    val id: Int? = null,
    @SerializedName("lecture_bank_id") val lectureBankId: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    val nickname: String? = null,
    val comments: String? = null,
    @SerializedName("created_at") val createdAt : String? = null,
    @SerializedName("updated_at") val updatedAt : String? = null,
)
