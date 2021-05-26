package `in`.hangang.core.util

import java.text.DecimalFormat
import kotlin.math.roundToLong

const val UNIT_BYTE_STRING = "Byte"
const val UNIT_KB_STRING = "KB"
const val UNIT_MB_STRING = "MB"
const val UNIT_GB_STRING = "GB"

const val UNIT_BYTE = 0
const val UNIT_KB = 1
const val UNIT_MB = 2
const val UNIT_GB = 3

fun Long.toProperCapacityUnit(round: Int = 0, startUnit: Int = UNIT_BYTE, maxUnit: Int = UNIT_GB): String {
    var unit = startUnit
    var value = this.toDouble()
    while (unit < maxUnit && value >= 1024) {
        value /= 1024.0
        unit++
    }

    return "${
        if(round > 0) {
            val format = StringBuilder("0.")
            for(i in 0 until round) format.append("#")
            DecimalFormat(format.toString()).format(value)
        } else {
            value.roundToLong()
        }
    }${
        when (unit) {
            UNIT_KB -> UNIT_KB_STRING
            UNIT_MB -> UNIT_MB_STRING
            UNIT_GB -> UNIT_GB_STRING
            else -> UNIT_BYTE_STRING
        }
    }"
}