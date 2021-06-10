package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName


class SignUpRequest(
        @SerializedName("major") var major: Array<String>,
        @SerializedName("nickname") var nickName: String,
        @SerializedName("password") var apassword: String,
        @SerializedName("portal_account") var portalAccount: String
)