package `in`.hangang.hangang.data.ranking

import `in`.hangang.hangang.data.ranking.RankingLectureHashTag
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder

@Parcelize
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
    val updatedAt: String,
    @SerializedName("code")
    val code: String,

    ) : Parcelable{
        public fun getTotalSemester(): String{
            var sb = StringBuilder()
            var max = semesterData.size
            semesterData.forEachIndexed{ idx, semester ->
                sb.append(semester)
                if(idx != max - 1){
                    sb.append(", ")
                }
            }
            return sb.toString()
        }
    }