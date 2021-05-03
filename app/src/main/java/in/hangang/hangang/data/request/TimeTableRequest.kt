package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class TimeTableRequest(
    @SerializedName("lecture_id")
    val lectureId: Int? = null,
    @SerializedName("user_timetable_id")
    val userTimeTableId: Int? = null,
    val id: Int? = null
)
