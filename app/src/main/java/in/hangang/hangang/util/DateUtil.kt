package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import android.content.Context
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import kotlin.math.abs

object DateUtil {
    //2021-05-27T14:44:57.000+00:00
    private const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    fun getTodayApiDate() = SimpleDateFormat(API_DATE_FORMAT).format(Date())

    fun apiDateToPeriodString(context: Context, apiDate: String) : String{
        val stringBuilder = StringBuilder()
        val apiDateTime = LocalDateTime.ofInstant(
            (SimpleDateFormat(API_DATE_FORMAT).parse(apiDate) ?: Date(System.currentTimeMillis())).toInstant(),
            ZoneId.of("Asia/Seoul")
        )
        val nowDateTime = LocalDateTime.now()

        val period = Period.between(nowDateTime.toLocalDate(), apiDateTime.toLocalDate())
        val duration = Duration.between(nowDateTime.toLocalTime(), apiDateTime.toLocalTime())
        val value = when {
            period.years != 0 -> {
                stringBuilder.append(context.getString(R.string.date_year, abs(period.years)))
                period.years.toLong()
            }
            period.months != 0 -> {
                stringBuilder.append(context.getString(R.string.date_month, abs(period.months)))
                period.months.toLong()
            }
            period.days != 0 -> {
                with(abs(period.days)) {
                    if(this >= 7) {
                        stringBuilder.append(context.getString(R.string.date_week, this / 7))
                        (period.days / 7).toLong()
                    } else {
                        stringBuilder.append(context.getString(R.string.date_day, abs(period.days)))
                        period.days.toLong()
                    }
                }
            }
            duration.toHours() != 0L -> {
                stringBuilder.append(context.getString(R.string.date_hour, abs(duration.toHours())))
                duration.toHours()
            }
            duration.toMinutes() != 0L -> {
                stringBuilder.append(context.getString(R.string.date_minute, abs(duration.toMinutes())))
                duration.toMinutes()
            }
            else -> {
                stringBuilder.append(context.getString(R.string.date_second, abs(duration.seconds)))
                duration.seconds
            }
        }

        stringBuilder.append(" ")
        if(value < 0) stringBuilder.append(context.getString(R.string.date_before))
        else stringBuilder.append(context.getString(R.string.date_after))

        return stringBuilder.toString()
    }
}