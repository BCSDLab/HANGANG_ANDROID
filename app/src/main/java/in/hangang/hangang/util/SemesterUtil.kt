package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import android.content.Context

class SemesterUtil {
    companion object {
        fun getSemesterText(context: Context, semesterDateId: Int): String {
            return when (semesterDateId) {
                1 -> context.getString(R.string.semester_20191)
                2 -> context.getString(R.string.semester_20192)
                3 -> context.getString(R.string.semester_20201)
                4 -> context.getString(R.string.semester_20202)
                5 -> context.getString(R.string.semester_20211)
                6 -> context.getString(R.string.semester_20212)
                else -> "Unknown"
            }
        }
    }
}