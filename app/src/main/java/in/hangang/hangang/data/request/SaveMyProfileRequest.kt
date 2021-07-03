package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class SaveMyProfileRequest(
    @SerializedName("name") var name: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("major") var major: ArrayList<String>
)