package `in`.hangang.hangang.data.lecturebank

import `in`.hangang.hangang.constant.LECTURE_BANKS_ORDER_BY_ID
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LectureBankFilter(
    val order: String = LECTURE_BANKS_ORDER_BY_ID,
    val categories : List<String>? = null
) : Parcelable