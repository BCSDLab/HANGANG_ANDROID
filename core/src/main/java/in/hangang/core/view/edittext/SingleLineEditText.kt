package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener

open class SingleLineEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val view = LayoutInflater.from(context).inflate(R.layout.layout_single_line_edit_text, this, true)

    protected val container: LinearLayout = view.findViewById<LinearLayout>(R.id.single_line_edit_text_container)
    val editText: EditText = view.findViewById<EditText>(R.id.single_line_edit_text)

    open var inputType: Int = EditorInfo.TYPE_CLASS_TEXT
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
            editText.hint = getString(R.styleable.SingleLineEditText_android_hint)
            editText.setText(getString(R.styleable.SingleLineEditText_android_text))
            editText.isEnabled = getBoolean(R.styleable.SingleLineEditText_android_enabled, true)

            recycle()
        }
    }

    var isEditTextEnabled
        set(value) {
            editText.isEnabled = value
        }
        get() = editText.isEnabled

    //This is editText methods
    var text: Editable
        set(value) {
            editText.text = value
        }
        get() = editText.text

    val selectionStart get() = editText.selectionStart
    val selectionEnd get() = editText.selectionEnd

    fun setText(s: CharSequence) {
        editText.setText(s)
    }

    fun setSelection(start: Int, end: Int) = editText.setSelection(start, end)
    fun setSelection(index: Int) = editText.setSelection(index)
    fun extendSelection(index: Int) = editText.extendSelection(index)
    fun selectAll() = editText.selectAll()

    fun addTextChangedListener(textWatcher: TextWatcher) = editText.addTextChangedListener(textWatcher)
    fun removeTextChangedListener(textWatcher: TextWatcher) = editText.removeTextChangedListener(textWatcher)

    inline fun addTextChangedListener(crossinline beforeTextChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
                                      crossinline onTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
                                      crossinline afterTextChanged: (text: Editable?) -> Unit = {}
    ): TextWatcher = editText.addTextChangedListener(beforeTextChanged, onTextChanged, afterTextChanged)
}

