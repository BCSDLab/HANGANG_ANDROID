package `in`.hangang.hangang.data.entity.evaluation

import com.google.gson.annotations.SerializedName

data class Chart(
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("count")
    val count: Int
)
