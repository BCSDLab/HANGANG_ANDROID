package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class EmailConfigRequest(
    @SerializedName("flag") val flag : Int,
    @SerializedName("portal_account") var portalAccount : String,
    @SerializedName("secret") var secret : String
)