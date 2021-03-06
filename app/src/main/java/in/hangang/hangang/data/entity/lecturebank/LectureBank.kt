package `in`.hangang.hangang.data.entity.lecturebank

import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.data.entity.user.User
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LectureBank(
    val id: Int,
    @SerializedName("user_id") val userId : Int,
    @SerializedName("lecture_id") val lectureId : Int,
    val category: List<String>,
    val title: String,
    val content: String,
    @SerializedName("point_price") val pointPrice: Int,
    @SerializedName("semester_date") val semesterDate : String,
    val hits: Int,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("is_deleted") val isDeleted : Boolean,
    @SerializedName("is_hit") val isHit : Boolean,
    @SerializedName("user_scrap_id") val userScrapId : Int,
    @SerializedName("is_purchase") val isPurchased : Boolean,
    val thumbnail: String,
    val user : User,
    val lecture: Lecture,
    val uploadFiles : List<UploadFile>?
) : Parcelable