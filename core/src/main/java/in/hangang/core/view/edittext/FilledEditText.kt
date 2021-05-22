package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import `in`.hangang.core.util.dp2Px
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat

class FilledEditText  @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attributeSet, defStyleAttr) {

    init {
        with(context) {
            setPadding(
                dp2Px(DEFAULT_HORIZONTAL_PADDING),
                dp2Px(DEFAULT_VERTICAL_PADDING),
                dp2Px(DEFAULT_VERTICAL_PADDING),
                dp2Px(DEFAULT_HORIZONTAL_PADDING))

            background = ContextCompat.getDrawable(this, R.drawable.filled_edit_text_background)
            backgroundTintList = ContextCompat.getColorStateList(this, R.color.filled_edit_text_background_color)
        }
    }

    companion object {
        const val DEFAULT_HORIZONTAL_PADDING = 8f
        const val DEFAULT_VERTICAL_PADDING = 12f
    }
}