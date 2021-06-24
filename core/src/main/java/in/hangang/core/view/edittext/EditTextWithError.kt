package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat


open class EditTextWithError @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SingleLineEditText(context, attributeSet, defStyleAttr, defStyleRes) {

    companion object {
        const val UNDEFINED = 0
        const val CHECK = 1
        const val ERROR = 2
    }

    private val checkIcon = ContextCompat.getDrawable(context, R.drawable.ic_check)
    private val errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_exclamation)

    private val statusImageView = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.edit_text_right_icon_size),
                resources.getDimensionPixelOffset(R.dimen.edit_text_right_icon_size))

        background = null
        container.addView(this)
    }

    var status = UNDEFINED
        set(value) {
            statusImageView.setImageDrawable(
                    when(value) {
                        CHECK -> checkIcon
                        ERROR -> errorIcon
                        else -> null
                    }
            )
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.EditTextWithError,
                defStyleAttr,
                defStyleRes
        ).apply {
            status = getInteger(R.styleable.EditTextWithError_status, UNDEFINED)

            recycle()
        }
    }
}