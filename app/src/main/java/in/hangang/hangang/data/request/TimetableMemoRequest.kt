package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class TimetableMemoRequest(
    @SerializedName("timetable_id")
    val timetableLectureId: Int,
    val memo: String? = null
)