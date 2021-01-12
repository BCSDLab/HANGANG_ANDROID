package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

open class SingleLineEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val view = LayoutInflater.from(context).inflate(R.layout.layout_single_line_edit_text, this, true)

    protected val container = view.findViewById<LinearLayout>(R.id.single_line_edit_text_container)
    protected val editText = view.findViewById<EditText>(R.id.single_line_edit_text)

    open var inputType : Int = EditorInfo.TYPE_CLASS_TEXT
        set(value) {
            editText.inputType = value
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.SingleLineEditText,
                defStyleAttr,
                defStyleRes
        ).apply {
            inputType = getInteger(R.styleable.SingleLineEditText_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
        }
    }
}