package `in`.hangang.hangang.data.lecture

import `in`.hangang.hangang.data.ranking.RankingLectureHashTag
import com.google.gson.annotations.SerializedName

data class RankingLectureItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("semester_data")
    val semesterData: List<String>,
    @SerializedName("top3_hash_tag")
    val top3HashTag: List<RankingLectureHashTag>,
    @SerializedName("name")
    val name: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("professor")
    val professor: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("total_rating")
    val totalRating: Int,
    @SerializedName("last_reviewed")
    val lastReviewed_at: String,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
)