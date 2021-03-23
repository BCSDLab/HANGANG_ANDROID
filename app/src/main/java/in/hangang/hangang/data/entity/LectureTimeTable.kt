package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName
import kotlin.Int

data class LectureTimeTable(
        val id : Int,
        @SerializedName("is_custom")
        val isCustom : Boolean,
        @SerializedName("semester_date")
        val semesterDate : String?,
        val code : String?,
        val name : String?,
        val classification : String?,
        val grades : String?,
        val classNumber : String?,
        @SerializedName("regular_number")
        val regularNumber : String?,
        val department : String?,
        val target : String?,
        val professor : String?,
        @SerializedName("is_english")
        val isEnglish : String?,
        @SerializedName("design_score")
        val designScore : String?,
        @SerializedName("is_elearning")
        val isElearning : String?,
        @SerializedName("class_time")
        val classTime : String?,
        @SerializedName("created_at")
        val createdAt : String?,
        @SerializedName("updated_at")
        val updatedAt : String?,
        val rating : Double
)
