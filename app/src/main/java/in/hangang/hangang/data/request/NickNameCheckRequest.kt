package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class NickNameCheckRequest(
        @SerializedName("nickname") var nickname: String
)