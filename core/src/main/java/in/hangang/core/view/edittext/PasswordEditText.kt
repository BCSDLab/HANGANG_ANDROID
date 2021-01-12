package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import androidx.core.content.ContextCompat


open class PasswordEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SingleLineEditText(context, attributeSet, defStyleAttr, defStyleRes) {

    private val maskedPasswordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password_masked)
    private val unmaskedPasswordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password_masked) //TODO : Replace drawable to unmasked password eye icon

    var masked = true
        set(value) {
            if (value) maskPassword()
            else unmaskPassword()
            field = value
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

    private val passwordMaskToggleButton = ImageButton(context, attributeSet, defStyleAttr, defStyleRes).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setOnClickListener { masked = !masked }
        background = null
        container.addView(this)
    }

    override var inputType: Int
        get() {
            return if (masked) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        set(value) {
            super.inputType = if (masked) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }

    private fun maskPassword() {
        passwordMaskToggleButton.setImageDrawable(maskedPasswordIcon)
    }

    private fun unmaskPassword() {
        passwordMaskToggleButton.setImageDrawable(unmaskedPasswordIcon)
    }
}