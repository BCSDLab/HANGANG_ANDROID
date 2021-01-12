package `in`.hangang.core.view.edittext

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout

open class SingleLineEditTextWithRightContainer @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SingleLineEditText(context, attributeSet, defStyleAttr, defStyleRes) {

    protected val rightContainer = LinearLayout(context, attributeSet, defStyleAttr, defStyleRes).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        gravity = Gravity.END or Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL
        setPadding(0, 0, 0, 0)
        container.addView(this)
    }
}