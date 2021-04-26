package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("portal_account")
    val portalAccount: String,
    val nickname: String,
    val major: List<String>,
    val authorityList: List<String>,
    val point: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)