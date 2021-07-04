package `in`.hangang.hangang.data.entity.lecturebank

import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.data.entity.user.User
import com.google.gson.annotations.SerializedName

data class LectureBankScrap(
    @SerializedName("scrap_id")
    val scrapId: Int,
    @SerializedName("scraped_at")
    val scrapedAt: String,
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
    val uploadFiles : List<UploadFile>
)

fun LectureBankScrap.toLectureBank(): LectureBank {
    with(this) {
        return LectureBank(id, userId, lectureId, category, title, content, pointPrice, semesterDate, hits, createdAt, updatedAt, isDeleted, isHit, userScrapId, isPurchased, thumbnail, user, lecture, uploadFiles)
    }
}