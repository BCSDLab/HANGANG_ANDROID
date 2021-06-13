package `in`.hangang.core.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
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

private fun <T> Uri.query(context: Context, func : (Cursor) -> Unit, error: () -> T) {
    context.contentResolver.query(this, null, null, null, null).use {
        if(it == null) error()
        if(!it!!.moveToFirst()) {
            it.close()
            error()
        }

        func(it)
        it.close()
    }
}

fun Uri.getDisplayName(context: Context): String? {
    var displayName : String? = null
    query(context, {
        displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }, {
        displayName = null
    })

    return displayName
}

fun Uri.getSize(context: Context): Long? {
    var size : Long? = null
    query(context, {
        size = it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
    }, {
        size = null
    })

    return size
}

