package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class PointRecord(
        val id : Int,
        @SerializedName("user_id")
        val userId: Int,
        val variance: Int,
        val title: String?,
        @SerializedName("created_at")
        val createdAt: String?
)