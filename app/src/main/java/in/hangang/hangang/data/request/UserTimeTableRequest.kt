package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class UserTimeTableRequest(
    val id : Int = 0,
    val name : String,
    @SerializedName("semester_date_id")
    val semesterDateId : Long = 0
)
