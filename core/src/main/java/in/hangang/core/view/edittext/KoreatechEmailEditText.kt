package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

open class KoreatechEmailEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SingleLineEditText(context, attributeSet, defStyleAttr, defStyleRes) {

    val koreatechEmailTextView = TextView(context, attributeSet, defStyleAttr, defStyleRes).apply {
        text = "@koreatech.ac.kr"
        container.addView(this)
        setPadding(0, 0, 0, 0)
    }

    override var inputType: Int
        get() = EditorInfo.TYPE_CLASS_TEXT
        set(value) {
            super.inputType = EditorInfo.TYPE_CLASS_TEXT
        }
}