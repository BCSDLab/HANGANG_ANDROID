package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName
import kotlin.Int

data class LectureTimeTable(
    val classNumber : String,
    @SerializedName("class_time")
        val classTime : String,
    val classification : String,
    val code : String,
    @SerializedName("created_at")
        val createdAt : Timestamp,
    val department : String,
    @SerializedName("design_score")
        val designScore : String,
    val grades : String,
    val id : Int,
    @SerializedName("is_elearning")
        val isElearning : String,
    @SerializedName("is_english")
        val isEnglish : String,
    val name : String,
    val professor : String,
    @SerializedName("regular_number")
        val regularNumber : String,
    @SerializedName("semester_date")
        val semesterDate : String,
    val target : String,
    @SerializedName("updated_at")
        val updatedAt : Timestamp
)
