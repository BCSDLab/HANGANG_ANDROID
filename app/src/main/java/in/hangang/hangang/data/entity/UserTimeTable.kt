package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class UserTimeTable(
    val name : String,
    @SerializedName("semester_date_id")
    val semesterDateId : Long
)
