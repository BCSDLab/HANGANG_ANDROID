package `in`.hangang.hangang.data.entity.semester

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Semester(
    val id: Int,
    @SerializedName("semester")
    val semesterName: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("is_regular")
    val isRegular: Int
): Parcelable