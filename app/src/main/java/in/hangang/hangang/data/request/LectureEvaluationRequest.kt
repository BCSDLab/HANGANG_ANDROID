package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class LectureEvaluationRequest(
    @SerializedName("assignment") val assignment: ArrayList<LectureEvaluationIdRequest>,
    @SerializedName("assignment_amount") val assignmentAmount: Int,
    @SerializedName("attendance_frequency") val attendanceFrequency: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("difficulty") val difficulty: Int,
    @SerializedName("grade_portion") val grade_portion: Int,
    @SerializedName("hash_tags") val hashTag: ArrayList<LectureEvaluationIdRequest>,
    @SerializedName("lecture_id") val lectureId: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("semester_id") val semesterId: Int
)