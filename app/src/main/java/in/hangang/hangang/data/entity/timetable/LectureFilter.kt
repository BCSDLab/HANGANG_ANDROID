package `in`.hangang.hangang.data.entity.timetable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LectureFilter(
        val classifications: List<String> = listOf(),
        val department: String? = null,
        val keyword: String? = null
) : Parcelable