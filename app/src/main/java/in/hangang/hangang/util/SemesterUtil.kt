package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import android.content.Context

const val SEMESTER_1 = 1
const val SEMESTER_SUMMER = 2
const val SEMESTER_2 = 3
const val SEMESTER_WINTER = 4

class SemesterUtil {
    companion object {
        fun getSemesterText(context: Context, semesterDateId: Int): String {
            return when (semesterDateId) {
                1 -> context.getString(R.string.semester_20191)
                2 -> context.getString(R.string.semester_20192)
                3 -> context.getString(R.string.semester_20201)
                4 -> context.getString(R.string.semester_20202)
                5 -> context.getString(R.string.semester_20211)
                else -> "Unknown"
            }
        }

        /**
         * semester
         * 1 -> 1학기
         * 2 -> 여름학기
         * 3 -> 2학기
         * 4 -> 겨울학기
         */
        fun getSemesterDateId(year: Int, semester: Int): Int {
            val exception = IllegalArgumentException("Cannot resolve SemesterDateId of given values")
            return when (year) {
                2019 -> when (semester) {
                    1 -> 1
                    3 -> 2
                    else -> throw exception
                }
                2020 -> when (semester) {
                    1 -> 3
                    3 -> 4
                    else -> throw exception
                }
                2021 -> when (semester) {
                    1 -> 5
                    else -> throw exception
                }
                else -> throw exception
            }
        }

        val currentSemester = 5
    }
}