package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.base.fragment.getColorFromAttr
import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.fragment_email_authentication.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmailAuthenticationFragment : ViewBindingFragment<FragmentEmailAuthenticationBinding>() {
    override val layoutId = R.layout.fragment_email_authentication

    private val emailAuthenticationViewModel: EmailAuthenticationViewModel by viewModel()

    private val editTextEmailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.buttonSendAuthNumber.isEnabled = s?.isNotEmpty() ?: false
        }
    }
    private val editTextEmailAuthNumberTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.buttonFinishEmailAuth.isEnabled = s?.isNotEmpty() ?: false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvent()

        with(emailAuthenticationViewModel) {
            sentEmailAuth.observe(viewLifecycleOwner) {
                if (it) {
                    button_send_auth_number.text =
                            getString(R.string.reset_password_resend_auth_number)
                    button_send_auth_number.appearence = RoundedCornerButton.OUTLINED
                    getColorFromAttr(R.attr.colorOnSurface)?.let { color ->
                        button_send_auth_number.setTextColor(color)
                    }
                    binding.editTextEmailAuthNumber.isEditTextEnabled = true
                } else {
                    button_send_auth_number.text =
                            getString(R.string.reset_password_send_auth_number)
                    button_send_auth_number.appearence = RoundedCornerButton.FILLED
                    getColorFromAttr(R.attr.colorOnPrimary)?.let { color ->
                        button_send_auth_number.setTextColor(color)
                    }
                    binding.editTextEmailAuthNumber.isEditTextEnabled = false
                }
            }
        }
    }

    private fun initEvent() {
        with(binding) {
            editTextEmail.addTextChangedListener(editTextEmailTextWatcher)
            editTextEmailAuthNumber.addTextChangedListener(editTextEmailAuthNumberTextWatcher)

            buttonSendAuthNumber.setOnClickListener {
                emailAuthenticationViewModel.sendAuthNumber(
                        portalAccount = "${editTextEmail.text}@koreatech.ac.kr",
                        onSuccess = {
                            if (emailAuthenticationViewModel.sentEmailAuth.value == true)
                                showResentEmailAuthNumberDialog()
                        },
                        onError = {
                            showEmailAuthFailedDialog()
                        }
                )
            }

            buttonFinishEmailAuth.setOnClickListener {
                emailAuthenticationViewModel.finishEmailAuth(
                        portalAccount = "${editTextEmail.text}@koreatech.ac.kr",
                        secret = editTextEmailAuthNumber.text.toString(),
                        onSuccess = { nextStep() },
                        onError = { showEmailAuthFailedDialog() }
                )
            }
        }
    }

    fun nextStep() {
        (activity as ChangePasswordActivity).nextPage("${binding.editTextEmail.text}@koreatech.ac.kr")
    }

    private fun showResentEmailAuthNumberDialog() {
        (activity as ActivityBase).showSimpleDialog(
                title = getString(R.string.reset_password_dialog_resent_title),
                message = getString(R.string.reset_password_dialog_check_portal_message),
                positiveButtonText = getString(R.string.ok),
                positiveButtonOnClickListener = { (activity as ActivityBase).dismissSimpleDialog() }
        )
    }

    private fun showEmailAuthFailedDialog() {
        (activity as ActivityBase).showSimpleDialog(
                title = getString(R.string.reset_password_error_auth),
                message = getString(R.string.reset_password_dialog_check_portal_message),
                positiveButtonText = getString(R.string.reset_password_retry_auth),
                positiveButtonOnClickListener = {
                    binding.editTextEmailAuthNumber.setText("")
                    (activity as ActivityBase).dismissSimpleDialog()
                }
        )
    }

}