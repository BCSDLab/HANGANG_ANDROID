package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class EmailRequest(
    @SerializedName("flag") val flag: Int,
    @SerializedName("portal_account") var portalAccount: String
)