package `in`.hangang.core.view.button.checkbox

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet

class VCheckBox @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CheckBox(context, attributeSet, defStyleAttr) {
    init {
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.v_checkbox, 0, 0, 0)
    }
}