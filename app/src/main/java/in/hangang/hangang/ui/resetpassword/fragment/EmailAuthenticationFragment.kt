package `in`.hangang.hangang.ui.resetpassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.resetpassword.activity.ResetPasswordActivity
import `in`.hangang.hangang.ui.resetpassword.viewmodel.EmailAuthenticationViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import java.lang.StringBuilder

class EmailAuthenticationFragment :
    ViewBindingFragment<FragmentEmailAuthenticationBinding>(R.layout.fragment_email_authentication) {

    private val emailAuthenticationViewModel: EmailAuthenticationViewModel by viewModel()

    private val editTextEmailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            emailAuthenticationViewModel.email.postValue(s.toString())
        }
    }
    private val editTextEmailAuthNumberTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            emailAuthenticationViewModel.authNumber.postValue(s.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextEmail.addTextChangedListener(editTextEmailTextWatcher)
            editTextEmailAuthNumber.addTextChangedListener(editTextEmailAuthNumberTextWatcher)
        }

        with(emailAuthenticationViewModel) {
            email.observe(viewLifecycleOwner) {
                binding.canSendAuthNumber = it.isNotEmpty()
            }

            authNumber.observe(viewLifecycleOwner) {
                binding.canFinishAuth = it.isNotEmpty()
            }

            sentEmailAuth.observe(viewLifecycleOwner) {
                binding.sentAuthNumber = it
            }

            finishedEmailAuth.observe(viewLifecycleOwner) {
                if (it) nextStep()
            }

            showResentEmailAuthNumberDialog.observe(viewLifecycleOwner) {
                showResentEmailAuthNumberDialog()
            }

            showEmailAuthFailedDialog.observe(viewLifecycleOwner) {
                showEmailAuthFailedDialog()
            }
        }
    }

    fun onClickSendAuthNumber(view: View) {
        emailAuthenticationViewModel.sendAuthNumber()
    }

    fun onClickFinishEmailAuth(view: View) {
        emailAuthenticationViewModel.finishEmailAuth()
    }

    fun nextStep() {
        (activity as ResetPasswordActivity).nextPage()
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