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
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop

class KoreatechEmailEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : EditTextWithError(context, attributeSet, defStyleAttr, defStyleRes) {

    val koreatechEmailTextView = TextView(context, null, defStyleAttr, defStyleRes).apply {
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        text = "@koreatech.ac.kr"
        includeFontPadding = false
        setPadding(0, 0, 0, 0)
        container.addView(this)
    }

    override var inputType: Int
        get() = EditorInfo.TYPE_CLASS_TEXT
        set(value) {
            super.inputType = EditorInfo.TYPE_CLASS_TEXT
        }
}