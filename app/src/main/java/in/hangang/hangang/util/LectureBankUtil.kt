package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

object LectureBankUtil {
    const val CELL = "cell"
    const val DOC = "doc"
    const val HWP = "hwp"
    const val JPG = "jpg"
    const val PDF = "pdf"
    const val PNG = "png"
    const val PPT = "ppt"
    const val SHOW = "show"
    const val TXT = "txt"
    const val XLS = "xls"
    const val ZIP = "zip"

    fun getLectureBankFileTypeImage(context: Context, fileType: String): Drawable? {
        return when (fileType) {
            CELL -> ContextCompat.getDrawable(context, R.drawable.ic_cell)
            DOC -> ContextCompat.getDrawable(context, R.drawable.ic_doc)
            HWP -> ContextCompat.getDrawable(context, R.drawable.ic_hwp)
            JPG -> ContextCompat.getDrawable(context, R.drawable.ic_jpg)
            PDF -> ContextCompat.getDrawable(context, R.drawable.ic_pdf)
            PNG -> ContextCompat.getDrawable(context, R.drawable.ic_png)
            PPT -> ContextCompat.getDrawable(context, R.drawable.ic_ppt)
            SHOW -> ContextCompat.getDrawable(context, R.drawable.ic_show)
            TXT -> ContextCompat.getDrawable(context, R.drawable.ic_txt)
            XLS -> ContextCompat.getDrawable(context, R.drawable.ic_xls)
            ZIP -> ContextCompat.getDrawable(context, R.drawable.ic_zip)
            else -> null
        }
    }
}