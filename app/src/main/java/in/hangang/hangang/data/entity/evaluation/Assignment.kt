package `in`.hangang.hangang.data.entity.evaluation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Assignment (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) : Parcelable{}