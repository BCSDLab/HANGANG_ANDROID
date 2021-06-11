package `in`.hangang.hangang.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HashTag(
        val id: Int,
        val tag: String
): Parcelable
