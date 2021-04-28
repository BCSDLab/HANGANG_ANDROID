package `in`.hangang.hangang.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LectureFilter(
    val criteria: String? = null,
    val classifications: List<String> = listOf(),
    val department: String? = null,
    val keyword: String? = null
) : Parcelable