package `in`.hangang.hangang.util

import `in`.hangang.core.view.timetable.TimetableLayout
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureTimeTable
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class TimetableUtil(private val context: Context) {

    val timetableColors = arrayOf(
            ContextCompat.getColor(context, R.color.timetable_color_1),
            ContextCompat.getColor(context, R.color.timetable_color_2),
            ContextCompat.getColor(context, R.color.timetable_color_3),
            ContextCompat.getColor(context, R.color.timetable_color_4),
            ContextCompat.getColor(context, R.color.timetable_color_5),
            ContextCompat.getColor(context, R.color.timetable_color_6),
            ContextCompat.getColor(context, R.color.timetable_color_7)
    )

    companion object {
        fun toString(context: Context, exp: String): String {
            val stringBuilder = StringBuilder()
            convert(exp) { f, l ->
                with(stringBuilder) {
                    if(this.isNotEmpty()) append(", ")
                    append(getWeekString(context, l))
                    append(" ")
                    append(getTimeString(f))
                    append("~")
                    append(getTimeString(l))
                }
            }

            return stringBuilder.toString()
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
                            if(it - stack.peek() >= 2) {
                                func(stack.firstElement(), stack.peek())
                                stack.clear()
                                stack.push(it)
                            } else {
                                stack.push(it)
                            }
                        }
                    }
            if(stack.isNotEmpty()) {
                func(stack.firstElement(), stack.peek())
            }
        }

        //월(0) ~ 일(6)
        private fun getWeek(value: Int): Float = (value / 100).toFloat()
        private fun getWeekString(context: Context, value: Int): String =
            when(value / 100) {
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
                    String.format("%02d", this / 2 + 1) + if(this % 2 == 0) "A" else "B"
                }
    }

    fun render(lectureTimeTables: List<LectureTimeTable>) : Single<List<View>> {
        return Single.create { subscriber ->
            try {
                val views = mutableListOf<View>()

                lectureTimeTables.forEachIndexed { i, lectureTimeTable ->
                    convertToTimes(lectureTimeTable.classTime ?: "[]").forEach { rc ->
                        views.add(
                                TextView(ContextThemeWrapper(context, R.style.HangangTimetableItem)).apply {
                                    setBackgroundColor(timetableColors[i % timetableColors.size])
                                    text = "${lectureTimeTable.name?: ""}\n${lectureTimeTable.classNumber ?: ""}${lectureTimeTable.professor?: ""}"
                                    layoutParams = TimetableLayout.LayoutParams(
                                            context, null,
                                            rc.rowStart,
                                            rc.rowEnd,
                                            rc.columnStart,
                                            rc.columnEnd
                                    )
                                }
                        )
                    }
                }

                subscriber.onSuccess(views)

            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    private fun convertToTimes(exp : String) : List<RC> {
        val rcs = mutableListOf<RC>()
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
                        if(it - stack.peek() >= 2) {
                            rcs.add(RC(
                                    columnStart = getWeek(stack.peek()),
                                    columnEnd = getWeek(stack.peek()) + 1,
                                    rowStart = getTime(stack.firstElement()),
                                    rowEnd = getTime(stack.peek() + 1)
                            ))
                            stack.clear()
                            stack.push(it)
                        } else {
                            stack.push(it)
                        }
                    }
                }
        if(stack.isNotEmpty()) {
            rcs.add(RC(
                    columnStart = getWeek(stack.peek()),
                    columnEnd = getWeek(stack.peek()) + 1,
                    rowStart = getTime(stack.firstElement()),
                    rowEnd = getTime(stack.peek() + 1)
            ))
        }
        return rcs
    }

    data class RC(val rowStart: Float, val rowEnd: Float, val columnStart: Float, val columnEnd: Float)
}