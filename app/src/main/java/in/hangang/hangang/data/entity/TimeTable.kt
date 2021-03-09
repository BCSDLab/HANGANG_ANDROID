package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class TimeTable(
        @SerializedName("lecture_id")
        val lectureId : Int,
        @SerializedName("user_timetable_id")
        val userTimetableId : Int
)