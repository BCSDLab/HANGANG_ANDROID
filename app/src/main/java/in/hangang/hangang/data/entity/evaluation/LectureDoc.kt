package `in`.hangang.hangang.data.entity.evaluation

import com.google.gson.annotations.SerializedName
data class LectureDocResult(
    @SerializedName("result")
    var result: ArrayList<LectureDoc>,
    @SerializedName("count")
    val count: Int

)
data class LectureDoc (
    @SerializedName("id")
    val id: Int,
    @SerializedName("lecture_id")
    val lectureId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)
