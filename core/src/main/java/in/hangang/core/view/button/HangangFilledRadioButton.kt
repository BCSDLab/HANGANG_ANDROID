package `in`.hangang.core.view.button

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RadioButton
import androidx.core.content.ContextCompat

class HangangFilledRadioButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatRadioButton(context, attributeSet, defStyleAttr) {

    init {
        minWidth = resources.getDimensionPixelSize(R.dimen.radio_min_width)
        gravity = Gravity.CENTER
        isClickable = true
        buttonDrawable = null
        setBackgroundResource(R.drawable.rounded_corner_rectangle_filled)
        backgroundTintList = ContextCompat.getColorStateList(context, R.color.color_state_radio_button_blue)
        setTextColor(ContextCompat.getColorStateList(context, R.color.color_state_radio_button_text))
    }
}