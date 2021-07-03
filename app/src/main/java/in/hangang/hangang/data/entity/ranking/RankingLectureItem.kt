package `in`.hangang.hangang.data.entity.ranking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder

data class RankingLectureResult(
    @SerializedName("result")
    val result: ArrayList<RankingLectureItem>,
    @SerializedName("count")
    val count: Int
)

@Parcelize
data class RankingLectureItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_scraped")
    val isScraped: Boolean,
    @SerializedName("grade")
    val grade: Int,
    @SerializedName("semester_data")
    val semesterData: List<String>,
    @SerializedName("top3_hash_tag")
    val top3HashTag: List<RankingLectureHashTag>,
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("professor")
    val professor: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("total_rating")
    val totalRating: String,
    @SerializedName("last_reviewed")
    val lastReviewed_at: String,
    @SerializedName("review_count")
    val reviewCount: String,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable {
    fun getTotalSemester(): String {
        var sb = StringBuilder()
        var max = semesterData.size
        semesterData.forEachIndexed { idx, semester ->
            sb.append(semester)
            if (idx != max - 1) {
                sb.append(", ")
            }
        }
        return sb.toString()
    }

    fun getStringGrade(): String {
        return "${grade}학점"
    }
}