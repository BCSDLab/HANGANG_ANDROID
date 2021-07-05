package `in`.hangang.hangang.data.entity.lecturebank

import com.google.gson.annotations.SerializedName

data class LectureBankPostRequest(
    val category: List<String>,
    val content: String,
    val files: List<String>,
    @SerializedName("lecture_id")
    val lectureId: Int,
    @SerializedName("semester_id")
    val semesterDateId: Int,
    val title: String
)
