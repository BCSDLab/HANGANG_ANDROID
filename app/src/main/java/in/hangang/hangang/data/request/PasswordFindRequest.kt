package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class PasswordFindRequest(
        @SerializedName("portal_account") var portalAccount: String,
        @SerializedName("password") var password: String
)