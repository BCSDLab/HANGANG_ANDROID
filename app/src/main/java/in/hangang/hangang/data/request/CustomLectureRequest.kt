package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class CustomLectureRequest(
        @SerializedName("class_time")
        val classTime: String?,
        val name: String?,
        val professor: String?,
        @SerializedName("user_timetable_id")
        val userTimetableId: Int
)