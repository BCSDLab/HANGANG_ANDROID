package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class ScrapLectureRequest(
    @SerializedName("id")
    val lectureId: Int
)