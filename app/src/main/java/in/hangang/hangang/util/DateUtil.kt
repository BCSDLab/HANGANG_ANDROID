package `in`.hangang.hangang.util

import `in`.hangang.core.R
import android.content.Context
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

object DateUtil {
    //2021-05-27T14:44:57.000+00:00
    private const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    const val MY_PAGE_POINT_RECORD_DATE_TIME = "yyyy/MM/dd HH:mm"

    fun getTodayApiDate() = SimpleDateFormat(API_DATE_FORMAT).format(Date())

    fun formatApiStringDateTime(apiDate: String?, format: String): String {
        val apiDateTime = LocalDateTime.ofInstant(
            apiDate?.let { SimpleDateFormat(API_DATE_FORMAT).parse(it).toInstant() } ?: Date(System.currentTimeMillis()).toInstant(),
            ZoneId.of("Asia/Seoul")
        )

        return apiDateTime.format(DateTimeFormatter.ofPattern(format))
    }
}