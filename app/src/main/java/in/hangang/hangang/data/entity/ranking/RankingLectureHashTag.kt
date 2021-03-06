package `in`.hangang.hangang.data.entity.ranking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RankingLectureHashTag
    (
    @SerializedName("id")
    val id: Int,
    @SerializedName("tag")
     val tag: String) : Parcelable{
         fun tagToString(): String{
             return "# ${tag}"
         }
     }
