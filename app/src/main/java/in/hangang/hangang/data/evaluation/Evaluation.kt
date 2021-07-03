package `in`.hangang.hangang.data.evaluation

import com.google.gson.annotations.SerializedName

class Evaluation(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("lecture_id")
    val lectureId: Int?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("semester_date")
    val semesterDate: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("is_liked")
    val isLiked: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("likes")
    val likes: String?,
    @SerializedName("assignment_amount")
    val assignmentAmount: Int?,
    @SerializedName("difficulty")
    val difficulty: Int?,
    @SerializedName("grade_portion")
    val gradePortion: Int?,
    @SerializedName("attendance_frequency")
    val attendanceFrequency: Int?,
    @SerializedName("test_times")
    val testTimes: String?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("hash_tags")
    val hashTags: String?,
    @SerializedName("assignment")
    val assignment: String?,
    @SerializedName("return_id")
    val returnId: String?,
    @SerializedName("is_deleted")
    val isDeleted: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,
    ) {
    fun attendanceToString(): String{
        return when(attendanceFrequency) {
            1 -> "하"
            2 -> "중"
            3 -> "상"
            else -> "unkown"
        }
    }
    fun difficultyToString(): String{
        return when(difficulty) {
            1 -> "하"
            2 -> "중"
            3 -> "상"
            else -> "unkown"
        }
    }
    fun assignmentAmountToString(): String{
        return when(assignmentAmount) {
            1 -> "하"
            2 -> "중"
            3 -> "상"
            else -> "unkown"
        }
    }
    fun gradePortionToString(): String{
        return when(gradePortion) {
            1 -> "아쉽게주심"
            2 -> "적당히주심"
            3 -> "후하게주심"
            else -> "unkown"
        }
    }

}
