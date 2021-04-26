package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class UploadFile(
    val id: Int,
    val fileName: String?,
    @SerializedName("ext")
    val fileExt: String?
)