package `in`.hangang.hangang.data.evaluation

import `in`.hangang.hangang.data.ranking.RankingLectureHashTag
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class LectureReviewResult(
    @SerializedName("result")
    var result: ArrayList<LectureReview>,
    @SerializedName("count")
    var count: Int
    )
data class LectureReview(
    @SerializedName("id")
    var id: Int,
    @SerializedName("lecture_id")
    var lectureId: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("semester_date")
    var semesterDate: String,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("rating")
    var rating: Float,
    @SerializedName("likes")
    var likes: Int,
    @SerializedName("assignment_amount")
    var assignmentAmount: Int,
    @SerializedName("difficulty")
    var difficulty: Int,
    @SerializedName("grade_portion")
    var gradePortion: Int,
    @SerializedName("attendance_frequency")
    var attendanceFrequency: Int,
    @SerializedName("test_times")
    var testTime: String?,
    @SerializedName("comment")
    var comment: String,
    @SerializedName("hash_tags")
    var hashTag: List<RankingLectureHashTag>,
    @SerializedName("assignment")
    var assignment: List<Assignment>,
    @SerializedName("return_id")
    var returnId: Int?,
    @SerializedName("is_deleted")
    var isDeleted: Boolean,
    @SerializedName("created_at")
    var createAt: String,
    @SerializedName("updated_at")
    var updateAt: String,
    @SerializedName("is_liked")
    var isLiked: Boolean
    ){
    fun assignmentToString(): String{
        var sb = StringBuilder()
        var max = assignment.size
        assignment.forEachIndexed { idx, assignment ->
            sb.append(assignment.name)
            if (idx != max - 1) {
                sb.append(", ")
            }
        }
        return sb.toString()
    }
}