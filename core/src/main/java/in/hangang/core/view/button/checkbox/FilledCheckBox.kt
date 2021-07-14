package `in`.hangang.core.view.button.checkbox

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat

class FilledCheckBox @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatCheckBox(context, attributeSet, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        isClickable = true
        buttonDrawable = null
        setBackgroundResource(R.drawable.rectangle_rounded_corner_filled)
        backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.color_state_radio_button_blue)
        setTextColor(
                ContextCompat.getColorStateList(context, R.color.color_state_radio_button_text)
        )
    }
}