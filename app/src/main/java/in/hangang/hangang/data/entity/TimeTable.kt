package `in`.hangang.hangang.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeTable(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("semester_date_id")
    val semesterDateId: Int,
    val name: String?,
    @SerializedName("is_deleted")
    val isDeleted: Boolean = false,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable