package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

    private fun initEvent() {
        with(binding) {
            editTextEmail.addTextChangedListener(editTextEmailTextWatcher)
            editTextEmailAuthNumber.addTextChangedListener(editTextEmailAuthNumberTextWatcher)

            buttonSendAuthNumber.setOnClickListener {
                emailAuthenticationViewModel.sendAuthNumber(
                    "${editTextEmail.text}@koreatech.ac.kr"
                )
            }

            buttonFinishEmailAuth.setOnClickListener {
                emailAuthenticationViewModel.finishEmailAuth(
                    "${editTextEmail.text}@koreatech.ac.kr",
                    editTextEmailAuthNumber.text.toString()
                )
            }
        }
    }

    fun nextStep() {
        (activity as ChangePasswordActivity).nextPage()
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