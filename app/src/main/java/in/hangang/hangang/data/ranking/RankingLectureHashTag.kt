package `in`.hangang.hangang.data.ranking

import com.google.gson.annotations.SerializedName

data class RankingLectureHashTag
    (
    @SerializedName("id")
    val id: Int,
    @SerializedName("tag")
     val tag: String)
