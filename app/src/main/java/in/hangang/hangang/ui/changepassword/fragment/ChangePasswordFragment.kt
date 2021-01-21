package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentNewPasswordBinding
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : ViewBindingFragment<FragmentNewPasswordBinding>() {

    var portalAccount : String = ""
    override val layoutId = R.layout.fragment_new_password

    private val changePasswordViewModel: ChangePasswordViewModel by viewModel()

    private val newPasswordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.textViewPasswordRegexErrorMessage.text =
                generatePasswordRegexErrorString(binding.editTextNewPassword.errorCode)
            binding.buttonFinishChangePassword.isEnabled =
                (binding.editTextNewPassword.errorCode == PasswordEditTextWithRegex.NO_ERR) and
                        binding.editTextConfirmNewPassword.text.isNotEmpty()
        }
    }
    private val newPasswordConfirmTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvent()
    }

    private fun initEvent() {
        with(binding) {
            editTextNewPassword.addTextChangedListener(newPasswordTextWatcher)
            editTextConfirmNewPassword.addTextChangedListener(newPasswordConfirmTextWatcher)

            buttonFinishChangePassword.setOnClickListener {
                changePasswordViewModel.applyNewPassword(portalAccount, editTextNewPassword.text.toString())
            }
        }
    }

    private fun generatePasswordRegexErrorString(errorCode: Int): String {
        return if (errorCode and PasswordEditTextWithRegex.MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS == PasswordEditTextWithRegex.ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS)
            getString(R.string.reset_password_regex_error_included_not_supported_characters)
        else if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NO_INPUT == PasswordEditTextWithRegex.ERR_NO_INPUT)
            getString(R.string.reset_password_regex_error_no_inputs)
        else if (errorCode and PasswordEditTextWithRegex.MASK_ERR_LENGTH != PasswordEditTextWithRegex.NO_ERR) {
            when (errorCode and PasswordEditTextWithRegex.MASK_ERR_LENGTH) {
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_SHORT -> getString(R.string.reset_password_regex_error_too_short)
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_LONG -> getString(R.string.reset_password_regex_error_too_long)
                else -> "."
            }
        } else {
            val stringBuilder = StringBuilder()

            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_ENGLISH == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_ENGLISH)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_english))
            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_NUMBER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_NUMBER)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_number))
            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_SPECIAL_CHARACTER)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_specials))

            getString(R.string.reset_password_regex_error_not_included, stringBuilder.toString())
        }
    }

}