package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class LoginRequest(
        @SerializedName("portal_account") val portalAccount: String,
        @SerializedName("password") val password: String
)