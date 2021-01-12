package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo

class MaxLengthEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SingleLineEditText(context, attributeSet, defStyleAttr, defStyleRes) {
    var maxLength = 9
        set(value) {
            editText.filters = arrayOf(InputFilter.LengthFilter(value))
            editText.hint = "${value + 1}자 미만"
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.EmsEditText,
                defStyleAttr,
                defStyleRes
        ).apply {
            maxLength = getInteger(R.styleable.EmsEditText_android_maxLength, 9)
        }
    }
}