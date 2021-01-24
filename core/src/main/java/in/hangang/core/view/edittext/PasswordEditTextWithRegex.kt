package `in`.hangang.core.view.edittext

import `in`.hangang.core.util.Regexps
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet


open class PasswordEditTextWithRegex @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : PasswordEditText(context, attributeSet, defStyleAttr, defStyleRes) {

    companion object {
        const val PASSWORD_LENGTH_MIN = 8
        const val PASSWORD_LENGTH_MAX = 15

        const val MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS =  0x1f000000
        const val MASK_ERR_LENGTH =                             0x10f00000
        const val MASK_ERR_NOT_CONTAINS_ENGLISH =               0x100f0000
        const val MASK_ERR_NOT_CONTAINS_NUMBER =                0x1000f000
        const val MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER =     0x10000f00
        const val MASK_ERR_NO_INPUT =                           0x100000f0

        const val ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS =       0x11000000
        const val ERR_LENGTH_TOO_SHORT =                        0x10100000
        const val ERR_LENGTH_TOO_LONG =                         0x10200000
        const val ERR_NOT_CONTAINS_ENGLISH =                    0x10010000
        const val ERR_NOT_CONTAINS_NUMBER =                     0x10001000
        const val ERR_NOT_CONTAINS_SPECIAL_CHARACTER =          0x10000100
        const val ERR_NO_INPUT =                                0x10000010

        const val NO_ERR = 0x10000000

        /* e.g.
        0x10000010 -> No input
        0x11xxxxxx -> Contains not supported characters
        0x10000100 -> Not contains special characters
        0x10101100 -> Length is too short and not contains number and special characters
        0x10200100 -> Length is too long and not contains special characters
         */
    }

    private var modified = false

    private var _errorCode = NO_ERR
        set(value) {
            status = if (value == NO_ERR) CHECK
            else if (value and MASK_ERR_NO_INPUT == ERR_NO_INPUT && !modified) UNDEFINED
            else ERROR

            field = value
        }
    val errorCode get() = _errorCode

    init {
        checkPassword(editText.text.toString())
        addTextChangedListener {
            modified = true
            checkPassword(it.toString())
        }
    }

    fun isErrorIncluded(errorCodeMask : Int) = errorCode and errorCodeMask != NO_ERR
    fun isErrorNotIncluded(errorCodeMask : Int) = !isErrorIncluded(errorCodeMask)

    private fun checkPassword(password: CharSequence) {
        _errorCode = checkPasswordContainsNotSupportedCharacter(password) or
                checkPasswordLength(password) or
                checkPasswordContainsEnglish(password) or
                checkPasswordContainsNumber(password) or
                checkPasswordContainsSymbol(password) or
                checkPasswordNoneInput(password)
    }

    private fun checkPasswordContainsNotSupportedCharacter(password: CharSequence) =
            if (Regexps.REGEX_MATCH_SUPPORTED_CHARACTERS.matchEntire(password) == null) ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS
            else NO_ERR

    private fun checkPasswordLength(password: CharSequence) =
            when {
                password.length < PASSWORD_LENGTH_MIN -> ERR_LENGTH_TOO_SHORT
                password.length > PASSWORD_LENGTH_MAX -> ERR_LENGTH_TOO_LONG
                else -> NO_ERR
            }

    private fun checkPasswordContainsEnglish(password: CharSequence) =
            if (Regexps.REGEX_CONTAINS_ENGLISH.matchEntire(password) == null) ERR_NOT_CONTAINS_ENGLISH
            else NO_ERR

    private fun checkPasswordContainsNumber(password: CharSequence) =
            if (Regexps.REGEX_CONTAINS_NUMBER.matchEntire(password) == null) ERR_NOT_CONTAINS_NUMBER
            else NO_ERR

    private fun checkPasswordContainsSymbol(password: CharSequence) =
            if (Regexps.REGEX_CONTAINS_SPECIAL_CHARACTER.matchEntire(password) == null) ERR_NOT_CONTAINS_SPECIAL_CHARACTER
            else NO_ERR

    private fun checkPasswordNoneInput(password: CharSequence) =
            if (password.isEmpty()) ERR_NO_INPUT
            else NO_ERR
}