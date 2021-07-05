package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

data class LectureBankReportRequest(
    @SerializedName("content_id") val contentId: Int,
    @SerializedName("report_id") val reportId: Int
)