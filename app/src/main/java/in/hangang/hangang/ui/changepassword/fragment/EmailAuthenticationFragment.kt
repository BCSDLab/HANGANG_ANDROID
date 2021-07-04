package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.base.fragment.getColorFromAttr
import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmailAuthenticationFragment : ViewBindingFragment<FragmentEmailAuthenticationBinding>() {
    override val layoutId = R.layout.fragment_email_authentication

    private val changePasswordActivityViewModel: ChangePasswordActivityViewModel by sharedViewModel()
    private val emailAuthenticationFragmentViewModel: EmailAuthenticationFragmentViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvent()
        initViewModel()
    }

    private fun initViewModel() {
        with(emailAuthenticationFragmentViewModel) {
            sentEmailAuth.observe(viewLifecycleOwner) {
                if (it) {
                    binding.buttonSendAuthNumber.text = getString(R.string.reset_password_resend_auth_number)
                    binding.buttonSendAuthNumber.appearence = RoundedCornerButton.OUTLINED
                    binding.buttonSendAuthNumber.setTextColor(getColorFromAttr(R.attr.colorOnSurface))
                    binding.editTextEmailAuthNumber.isEditTextEnabled = true
                } else {
                    binding.buttonSendAuthNumber.text = getString(R.string.reset_password_send_auth_number)
                    binding.buttonSendAuthNumber.appearence = RoundedCornerButton.FILLED
                    binding.buttonSendAuthNumber.setTextColor(getColorFromAttr(R.attr.colorOnPrimary))
                    binding.editTextEmailAuthNumber.isEditTextEnabled = false
                }
            }
            sendAuthNumberResponse.observe(viewLifecycleOwner) {

            }
            resendAuthNumberResponse.observe(viewLifecycleOwner) {
                showResentEmailAuthNumberDialog()
            }
            finishEmailAuthResponse.observe(viewLifecycleOwner) {
                changePasswordActivityViewModel.nextPage()
            }
            throwable.observe(viewLifecycleOwner) {
                showEmailAuthFailedDialog()
            }
        }
    }

    private fun initEvent() {
        with(binding) {
            editTextEmail.addTextChangedListener {
                binding.buttonSendAuthNumber.isEnabled = it?.isNotEmpty() ?: false
            }
            editTextEmailAuthNumber.addTextChangedListener {
                binding.buttonFinishEmailAuth.isEnabled = it?.isNotEmpty() ?: false
            }
            buttonSendAuthNumber.setOnClickListener {
                emailAuthenticationFragmentViewModel.sendAuthNumber(
                    portalAccount = "${editTextEmail.text}@koreatech.ac.kr"
                )
            }
            buttonFinishEmailAuth.setOnClickListener {
                emailAuthenticationFragmentViewModel.finishEmailAuth(
                    portalAccount = "${editTextEmail.text}@koreatech.ac.kr",
                    secret = editTextEmailAuthNumber.text.toString()
                )
            }
        }
    }

    private fun showResentEmailAuthNumberDialog() {
        activity?.showSimpleDialog(
            title = getString(R.string.reset_password_dialog_resent_title),
            message = getString(R.string.reset_password_dialog_check_portal_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun showEmailAuthFailedDialog() {
        activity?.showSimpleDialog(
            title = getString(R.string.reset_password_error_auth),
            message = getString(R.string.reset_password_dialog_check_portal_message),
            positiveButtonText = getString(R.string.reset_password_retry_auth),
            positiveButtonOnClickListener = { dialog, _ ->
                binding.editTextEmailAuthNumber.setText("")
                dialog.dismiss()
            }
        )
    }
}