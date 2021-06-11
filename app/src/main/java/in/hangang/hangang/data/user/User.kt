package `in`.hangang.hangang.data.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    @SerializedName("portal_account")
    val portalAccount: String,
    val major: List<String>,
    val point: Int,
    @SerializedName("is_deleted")
    val isDeleted : Boolean,
    val nickname: String
) : Parcelable