package `in`.hangang.core.view
import android.app.ProgressDialog.show
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import kotlin.math.roundToInt

fun View.showPopupMenu(@MenuRes menuId: Int) : PopupMenu {
    return PopupMenu(context, this).apply {
        inflate(menuId)
    }.also { it.show() }
}

fun View.dp2Px(dp: Float) = (dp * resources.displayMetrics.density).roundToInt()

fun View.sp2Px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).roundToInt()
}

fun View.setVisibility(visible: Boolean) {
    visibility =
            if (visible) View.VISIBLE
            else View.GONE
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
    for (i in 0 until childCount) {
        list.add(getChildAt(i))
    }
    return list
}
