package `in`.hangang.core.view

import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import kotlin.math.abs
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

fun calculateRectOnScreen(view: View): Rect {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    return Rect(
        location[0],
        location[1],
        location[0] + view.measuredWidth,
        location[1] + view.measuredHeight
    )
}

fun ViewGroup.childViews(): List<View> {
    val list = mutableListOf<View>()
    for(i in 0 until childCount) {
        list.add(getChildAt(i))
    }
    return list
}