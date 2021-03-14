package `in`.hangang.core.view

import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt


fun View.dp2Px(dp: Float) = (dp * resources.displayMetrics.density).roundToInt()

fun View.sp2Px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).roundToInt()
}

fun visibleGone(boolean: Boolean) = if (boolean) {
    View.VISIBLE
} else {
    View.GONE
}

fun visibleInvisible(boolean: Boolean) = if (boolean) {
    View.VISIBLE
} else {
    View.INVISIBLE
}

fun goneVisible(boolean: Boolean) = if (boolean) {
    View.GONE
} else {
    View.VISIBLE
}

fun invisibleVisible(boolean: Boolean) = if (boolean) {
    View.INVISIBLE
} else {
    View.VISIBLE
}