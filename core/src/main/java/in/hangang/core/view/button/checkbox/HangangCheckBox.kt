package `in`.hangang.core.view.button.checkbox

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.CheckBox

class HangangCheckBox @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatCheckBox(context, attributeSet, defStyleAttr) {

    init {
        isClickable = true
        buttonDrawable = null
        background = null
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_checkbox, 0, 0, 0)
        compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.checkbox_button_padding)
        gravity = Gravity.CENTER_VERTICAL
    }
}