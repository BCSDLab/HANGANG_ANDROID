package `in`.hangang.hangang.util

import `in`.hangang.core.view.timetable.TimetableLayout
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.LectureTimeTable
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.core.Single
import java.util.*

object TimetableUtil {
    private val timetableColors = arrayOf(
        R.color.timetable_color_1,
        R.color.timetable_color_2,
        R.color.timetable_color_3,
        R.color.timetable_color_4,
        R.color.timetable_color_5,
        R.color.timetable_color_6,
        R.color.timetable_color_7
    )

    fun toExp(timestamps: List<CustomTimetableTimestamp>): String {
        val set = mutableSetOf<Int>()
        timestamps.forEach {
            val startTime = (it.startTime.first - 9) * 2 + if (it.startTime.second < 30) 0 else 1
            val endTime = (it.endTime.first - 9) * 2 + if (it.endTime.second < 30) 0 else 1

            for (i in startTime until endTime) {
                set.add(it.week * 100 + i)
            }
        }
        return set.toList().sorted().joinToString(
            prefix = "[",
            postfix = "]",
            separator = ", "
        )
    }

    fun toString(context: Context, exp: String): String {
        val stringBuilder = StringBuilder()
        convert(exp) { f, l ->
            with(stringBuilder) {
                if (this.isNotEmpty()) append(", ")
                append(getWeekString(context, l))
                append(" ")
                append(getTimeString(f))
                append("~")
                append(getTimeString(l))
            }
        }

        return stringBuilder.toString()
    }

    fun isLectureTimetableTimeDuplicate(classTime1: String, classTime2: String): Boolean {
        val time2List = classTime2.substring(1, classTime2.length - 1)
            .splitToSequence(", ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toList()
        return classTime1.substring(1, classTime1.length - 1)
            .splitToSequence(", ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .any {
                time2List.contains(it)
            }
    }

    private fun convert(exp: String, func: (Int, Int) -> Unit) {
        val stack = Stack<Int>()
        exp.substring(1, exp.length - 1)
            .splitToSequence(", ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .sorted()
            .forEach {
                if (stack.empty())
                    stack.push(it)
                else {
                    if (it - stack.peek() >= 2) {
                        func(stack.firstElement(), stack.peek())
                        stack.clear()
                        stack.push(it)
                    } else {
                        stack.push(it)
                    }
                }
            }
        if (stack.isNotEmpty()) {
            func(stack.firstElement(), stack.peek())
        }
    }

    fun getTimetableTextView(
        context: Context,
        lectureTimeTables: List<LectureTimeTable>
    ): Single<Map<View, LectureTimeTable>> {
        return Single.create { subscriber ->
            try {
                val views = hashMapOf<View, LectureTimeTable>()

                lectureTimeTables.forEachIndexed { i, lectureTimeTable ->
                    convertToTimes(lectureTimeTable.classTime ?: "[]").forEach { rc ->
                        views[TimetableLayout.generateTimetableTextView(
                            context = context,
                            backgroundColor = ContextCompat.getColor(context, timetableColors[i % timetableColors.size]),
                            textColor = ContextCompat.getColor(context, R.color.text_color_timetable_item),
                            text = "${lectureTimeTable.name ?: ""}\n${lectureTimeTable.classNumber ?: ""} ${lectureTimeTable.professor ?: ""}",
                            rowColumn = rc
                        )] = lectureTimeTable
                    }
                }

                subscriber.onSuccess(views)

            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun getTimetableDummyView(
        context: Context,
        lectureTimeTables: List<LectureTimeTable>
    ): Single<List<View>> {
        return Single.create { subscriber ->
            try {
                val views = mutableListOf<View>()

                lectureTimeTables.forEachIndexed { i, lectureTimeTable ->
                    convertToTimes(lectureTimeTable.classTime ?: "[]").forEach { rc ->
                        views.add(TimetableLayout.generateTimetableDummyView(context, rc))
                    }
                }

                subscriber.onSuccess(views)

            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    //월(0) ~ 일(6)
    private fun getWeek(value: Int): Float = (value / 100).toFloat()
    private fun getWeekString(context: Context, value: Int): String =
        when (value / 100) {
            0 -> context.getString(R.string.timetable_mon)
            1 -> context.getString(R.string.timetable_tue)
            2 -> context.getString(R.string.timetable_wed)
            3 -> context.getString(R.string.timetable_thu)
            4 -> context.getString(R.string.timetable_fri)
            5 -> context.getString(R.string.timetable_sat)
            6 -> context.getString(R.string.timetable_sun)
            else -> ""
        }

    private fun getTime(value: Int): Float = (value % 100) / 2f
    private fun getTimeString(value: Int) =
        with(value % 100) {
            String.format("%02d", this / 2 + 1) + if (this % 2 == 0) "A" else "B"
        }

    private fun convertToTimes(exp: String): List<TimetableLayout.RC> {
        val rcs = mutableListOf<TimetableLayout.RC>()
        convert(exp) { f, l ->
            rcs.add(
                TimetableLayout.RC(
                    columnStart = getWeek(l),
                    columnEnd = getWeek(l) + 1,
                    rowStart = getTime(f),
                    rowEnd = getTime(l + 1)
                )
            )
        }
        return rcs
    }
}