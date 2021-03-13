package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.entity.Timestamp
import com.google.gson.annotations.SerializedName

data class TimeTableResponse(
    val id : Long,
    @SerializedName("user_id")
    val userId : Any,
    @SerializedName("semester_date_id")
    val semesterDateId : Long,
    val name : String,
    @SerializedName("is_deleted")
    val isDeleted : Boolean,
    @SerializedName("created_at")
    val createdAt : Timestamp,
    @SerializedName("updated_at")
    val updatedAt : Timestamp
)
