package `in`.hangang.hangang.data.response

import com.google.gson.annotations.SerializedName

class TokenResponse : CommonResponse() {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("refresh_token")
    var refreshToken: String? = null
}