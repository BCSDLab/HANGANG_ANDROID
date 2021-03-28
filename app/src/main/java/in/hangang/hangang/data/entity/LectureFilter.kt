package `in`.hangang.hangang.data.entity

import android.location.Criteria
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LectureFilter(
    val criteria: Int,
    val classifications : List<String>,
    val department: String?,
    val keyword: String?
) : Parcelable {
    companion object {
        const val CRITERIA_NONE = 0x00
        const val CRITERIA_NAME = 0x10
        const val CRITERIA_PROFESSOR = 0x01
        const val CRITERIA_NAME_PROFESSOR = 0x11
    }
}