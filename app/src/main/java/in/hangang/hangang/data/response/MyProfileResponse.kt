package `in`.hangang.hangang.data.response

import com.google.gson.annotations.SerializedName


data class MyProfileResponse(
    @SerializedName("portal_account")
    var portalAccount: String,

    @SerializedName("nickname")
    var nickname: String,

    @SerializedName("major")
    var major: Array<String>,

    @SerializedName("name")
    var name: String? = null

)