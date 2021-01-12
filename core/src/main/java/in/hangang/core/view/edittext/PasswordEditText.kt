package `in`.hangang.core.view.edittext

import `in`.hangang.core.R
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat


open class PasswordEditText @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : EditTextWithError(context, attributeSet, defStyleAttr, defStyleRes) {

    private val maskedPasswordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password_masked)
    private val unmaskedPasswordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password_unmasked)

    var masked = true
        set(value) {
            if (value) maskPassword()
            else unmaskPassword()
            field = value
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

    private val passwordMaskToggleButton = ImageButton(context).apply {
        layoutParams = LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.edit_text_right_icon_size),
                resources.getDimensionPixelOffset(R.dimen.edit_text_right_icon_size))

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

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.PasswordEditText,
                defStyleAttr,
                defStyleRes
        ).apply {
            masked = getBoolean(R.styleable.PasswordEditText_mask, true)
        }
    }

    private fun maskPassword() {
        passwordMaskToggleButton.setImageDrawable(maskedPasswordIcon)
    }

    private fun unmaskPassword() {
        passwordMaskToggleButton.setImageDrawable(unmaskedPasswordIcon)
    }
}