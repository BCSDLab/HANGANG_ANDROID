package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.ERR_NOT_CONTAINS_ENGLISH
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.ERR_NOT_CONTAINS_NUMBER
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.ERR_NOT_CONTAINS_SPECIAL_CHARACTER
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_LENGTH
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_NOT_CONTAINS_ENGLISH
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_NOT_CONTAINS_NUMBER
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_NO_INPUT
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.databinding.FragmentNewPasswordBinding
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : ViewBindingFragment<FragmentNewPasswordBinding>() {
    override val layoutId = R.layout.fragment_new_password

    private val changePasswordActivityViewModel: ChangePasswordActivityViewModel by sharedViewModel()
    private val changePasswordFragmentViewModel: ChangePasswordFragmentViewModel by viewModel()
    private val emailAuthenticationFragmentViewModel: EmailAuthenticationFragmentViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvent()
        initViewModel()
    }

    private fun initViewModel() {
        with(changePasswordFragmentViewModel) {
            changePasswordResponse.observe(viewLifecycleOwner) {
                changePasswordActivityViewModel.nextPage()
                showResetPasswordFinishedDialog()
            }
        }
    }

    private fun initEvent() {
        with(binding) {
            editTextNewPassword.addTextChangedListener {
                binding.textViewPasswordRegexErrorMessage.text =
                        generatePasswordRegexErrorString()
                binding.buttonFinishChangePassword.isEnabled =
                        (binding.editTextNewPassword.errorCode == PasswordEditTextWithRegex.NO_ERR) and
                                binding.editTextConfirmNewPassword.text.isNotEmpty()
            }
            editTextConfirmNewPassword.addTextChangedListener {
                binding.buttonFinishChangePassword.isEnabled =
                        (binding.editTextNewPassword.errorCode == PasswordEditTextWithRegex.NO_ERR) and
                                binding.editTextConfirmNewPassword.text.isNotEmpty()
            }
            buttonFinishChangePassword.setOnClickListener {
                changePasswordFragmentViewModel.applyNewPassword(
                        portalAccount = "${emailAuthenticationFragmentViewModel.portalAccount.value}@koreatech.ac.kr",
                        password = editTextNewPassword.text.toString()
                )
            }
        }
    }

    private fun generatePasswordRegexErrorString(): String =
            with(binding.editTextNewPassword) {
                when {
                    errorCode == PasswordEditTextWithRegex.NO_ERR ->
                        ""
                    isErrorIncluded(MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS) ->
                        getString(R.string.reset_password_regex_error_included_not_supported_characters)
                    isErrorIncluded(MASK_ERR_NO_INPUT) ->
                        getString(R.string.reset_password_regex_error_no_inputs)
                    isErrorIncluded(MASK_ERR_LENGTH) ->
                        generatePasswordRegexLengthErrorString(errorCode)
                    else ->
                        generatePasswordRegexNotIncludedSomeCharactersError(errorCode)
                }
            }

    private fun generatePasswordRegexLengthErrorString(errorCode: Int) =
            when (errorCode and MASK_ERR_LENGTH) {
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_SHORT -> getString(R.string.reset_password_regex_error_too_short)
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_LONG -> getString(R.string.reset_password_regex_error_too_long)
                else -> ""
            }

    private fun generatePasswordRegexNotIncludedSomeCharactersError(errorCode: Int): String {
        val stringBuilder = StringBuilder()

        if (errorCode and MASK_ERR_NOT_CONTAINS_ENGLISH == ERR_NOT_CONTAINS_ENGLISH)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_english))
        if (errorCode and MASK_ERR_NOT_CONTAINS_NUMBER == ERR_NOT_CONTAINS_NUMBER)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_number))
        if (errorCode and MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER == ERR_NOT_CONTAINS_SPECIAL_CHARACTER)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_specials))

        return getString(R.string.reset_password_regex_error_not_included, stringBuilder.toString())
    }


    private fun showResetPasswordFinishedDialog() {
        //TODO : message에 닉네임 표시 추가
        activity?.showSimpleDialog(
                title = getString(R.string.reset_password_finished_title),
                message = getString(R.string.reset_password_finished_message),
                positiveButtonText = getString(R.string.reset_password_finished_positive_button),
                positiveButtonOnClickListener = { _, _ ->
                    activity?.finish()
                }
        )
    }
}