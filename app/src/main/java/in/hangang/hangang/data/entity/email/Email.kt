package `in`.hangang.hangang.data.entity.email

import com.google.gson.annotations.SerializedName

data class Email (
    @SerializedName("flag")
    val flag: Int,
    @SerializedName("portal_account")
    val portalAccount: String
        )