package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class LecturesParameter(
    val classification: String? = null,
    val department: String? = null,
    @SerializedName("hash_tag")
    val hashTag: Int? = null,
    val keyword: String? = null,
    val limit: Int = 10,
    var page: Int = 1,
    val sort: String? = null
)