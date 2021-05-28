package `in`.hangang.hangang.data.evaluation

import com.google.gson.annotations.SerializedName

data class TimetableMemo(
    val id: Int?,
    @SerializedName("timetable_id")
    val timetableLectureId: Int?,
    val memo: String?,
    @SerializedName("is_deleted")
    val isDeleted: Boolean?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)