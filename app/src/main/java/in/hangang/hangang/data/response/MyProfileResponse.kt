package `in`.hangang.hangang.data.response

import com.google.gson.annotations.SerializedName


data class MyProfileResponse(
    @SerializedName("portal_account")
    var portalAccount: String? = null,

    @SerializedName("nickname")
    var nickname: String? = null,

    @SerializedName("major")
    var major: String? = null,

    @SerializedName("name")
    var name: String? = null

)