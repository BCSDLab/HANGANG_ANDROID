package `in`.hangang.core.view

import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt


fun View.dp2Px(dp: Float) = (dp * resources.displayMetrics.density).roundToInt()

fun View.sp2Px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).roundToInt()
}

fun View.visibleGone(boolean: Boolean) {
    visibility = if(boolean) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.visibleInvisible(boolean: Boolean) {
    visibility = if(boolean) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun View.goneVisible(boolean: Boolean) {
    visibility = if(boolean) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.invisibleVisible(boolean: Boolean) {
    visibility = if(boolean) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}