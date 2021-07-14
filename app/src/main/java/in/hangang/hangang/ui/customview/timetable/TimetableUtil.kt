package `in`.hangang.hangang.ui.customview.timetable

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.timetable.CustomTimetableTimestamp
import android.content.Context
import java.util.*

object TimetableUtil {
    val timetableColors = arrayOf(
            R.color.timetable_color_1,
            R.color.timetable_color_2,
            R.color.timetable_color_3,
            R.color.timetable_color_4,
            R.color.timetable_color_5,
            R.color.timetable_color_6,
            R.color.timetable_color_7
    )

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

    fun convertCustomTimetableTimestampToApiExpression(timestamps: List<CustomTimetableTimestamp>): String {
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

    fun convertApiExpressionToKoreatechClassTime(context: Context, exp: String): String {
        val stringBuilder = StringBuilder()
        convertExpression(exp) { f, l ->
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

    fun convertApiExpressionToRC(apiExpression: String): List<TimetableLayout.RC> {
        val rcs = mutableListOf<TimetableLayout.RC>()
        convertExpression(apiExpression) { f, l ->
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

    private fun convertExpression(exp: String, callback: (Int, Int) -> Unit) {
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
                            callback(stack.firstElement(), stack.peek())
                            stack.clear()
                            stack.push(it)
                        } else {
                            stack.push(it)
                        }
                    }
                }
        if (stack.isNotEmpty()) {
            callback(stack.firstElement(), stack.peek())
        }
    }

    //월(0) ~ 일(6)
    private fun getWeek(value: Int): Float = (value / 100).toFloat()
    private fun getWeekString(context: Context, value: Int): String =
            when (value / 100) {
                0 -> context.getString(R.string.mon)
                1 -> context.getString(R.string.tue)
                2 -> context.getString(R.string.wed)
                3 -> context.getString(R.string.thu)
                4 -> context.getString(R.string.fri)
                5 -> context.getString(R.string.sat)
                6 -> context.getString(R.string.sun)
                else -> ""
            }

    private fun getTime(value: Int): Float = (value % 100) / 2f
    private fun getTimeString(value: Int) =
            with(value % 100) {
                String.format("%02d", this / 2 + 1) + if (this % 2 == 0) "A" else "B"
            }
}