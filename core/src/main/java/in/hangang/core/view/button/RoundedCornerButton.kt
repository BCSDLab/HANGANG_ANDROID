package `in`.hangang.core.view.button

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes

class RoundedCornerButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attributeSet, defStyleAttr) {

    enum class Appearence {
        FILLED, OUTLINED
    }

    var appearence: Appearence = Appearence.FILLED
        set(value) {
            when (value) {
                Appearence.FILLED -> setBackgroundResource(R.drawable.rounded_corner_rectangle_filled)
                Appearence.OUTLINED -> setBackgroundResource(R.drawable.rounded_corner_rectangle_outline)
            }
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.RoundedCornerButton,
            defStyleAttr,
            0
        ).apply {
            appearence =
                if (getInteger(R.styleable.RoundedCornerButton_buttonAppearence, 0) == 0)
                    Appearence.FILLED
                else
                    Appearence.OUTLINED
        }
    }
}