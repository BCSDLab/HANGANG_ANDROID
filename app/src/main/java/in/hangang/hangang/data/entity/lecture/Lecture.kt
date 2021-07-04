package `in`.hangang.hangang.data.entity.lecture

import `in`.hangang.hangang.data.entity.timetable.HashTag
import `in`.hangang.hangang.data.entity.lecture.Lecture
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LectureResult(
    val result: List<Lecture>,
    val count: Int
)

@Parcelize
data class Lecture(
    val id: Int,
    @SerializedName("semester_data")
        val semesters: List<String>,
    @SerializedName("top3_hash_tag")
        val top3HashTag: List<HashTag>,
    val code: String,
    val name: String,
    val department: String,
    val professor: String,
    val classification: String,
    @SerializedName("total_rating")
        val totalRating: Double,
    @SerializedName("last_reviewed_at")
        val lastReviewedAt: String?,
    @SerializedName("review_count")
        val reviewCount: Int?,
    @SerializedName("is_deleted")
        val isDeleted: Boolean,
    @SerializedName("created_at")
        val createdAt: String,
    @SerializedName("updated_at")
        val updatedAt: String
): Parcelable