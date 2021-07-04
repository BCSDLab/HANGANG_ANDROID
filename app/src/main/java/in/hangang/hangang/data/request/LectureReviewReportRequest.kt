package `in`.hangang.hangang.data.request

import com.google.gson.annotations.SerializedName

class LectureReviewReportRequest(
    @SerializedName("content_id") val contentId: Int,
    @SerializedName("report_id") val reportId: Int
)