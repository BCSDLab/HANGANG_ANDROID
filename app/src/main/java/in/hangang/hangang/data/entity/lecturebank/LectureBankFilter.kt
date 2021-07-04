package `in`.hangang.hangang.data.entity.lecturebank

import `in`.hangang.hangang.constant.LECTURE_BANKS_ORDER_BY_ID
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LectureBankFilter(
    val order: String = LECTURE_BANKS_ORDER_BY_ID,
    val categories : List<String>? = null
) : Parcelable