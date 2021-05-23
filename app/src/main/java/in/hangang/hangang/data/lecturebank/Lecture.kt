package `in`.hangang.hangang.data.lecturebank

import com.google.gson.annotations.SerializedName

data class Lecture(
    @SerializedName("is_scraped") val isScraped: Boolean,
    val code: String,
    val name: String,
    val department: String,
    val professor: String,
    val classification: String
)
