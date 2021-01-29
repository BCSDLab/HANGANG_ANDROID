package `in`.hangang.core.view.button

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat

class RoundedCornerButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attributeSet, defStyleAttr) {

    companion object {
        const val FILLED = 0
        const val OUTLINED = 1
    }

    var appearence: Int = FILLED
        set(value) {
            when (value) {
                FILLED -> setBackgroundResource(R.drawable.rectangle_rounded_corner_filled)
                OUTLINED -> setBackgroundResource(R.drawable.rectangle_rounded_corner_outline_24dp)
            }
            if(value == FILLED){
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    ))
            }else{
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue_500
                    ))
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
            appearence = getInteger(R.styleable.RoundedCornerButton_buttonAppearence, 0)

            recycle()
        }
    }
}