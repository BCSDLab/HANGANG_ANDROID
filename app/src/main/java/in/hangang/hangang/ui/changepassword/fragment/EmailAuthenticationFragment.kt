package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.base.fragment.getColorFromAttr
import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_email_authentication.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmailAuthenticationFragment : ViewBindingFragment<FragmentEmailAuthenticationBinding>() {
    override val layoutId = R.layout.fragment_email_authentication

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
                    button_send_auth_number.text =
                            getString(R.string.reset_password_resend_auth_number)
                    button_send_auth_number.appearence = RoundedCornerButton.OUTLINED
                    button_send_auth_number.setTextColor(getColorFromAttr(R.attr.colorOnSurface))
                    binding.editTextEmailAuthNumber.isEditTextEnabled = true
                } else {
                    button_send_auth_number.text =
                            getString(R.string.reset_password_send_auth_number)
                    button_send_auth_number.appearence = RoundedCornerButton.FILLED
                    button_send_auth_number.setTextColor(getColorFromAttr(R.attr.colorOnPrimary))
                    binding.editTextEmailAuthNumber.isEditTextEnabled = false
                }
            }
        }
    }

    private fun initEvent() {
        with(binding) {
            editTextEmail.addTextChangedListener {
                binding.buttonSendAuthNumber.isEnabled = it?.isNotEmpty() ?: false
                emailAuthenticationFragmentViewModel.portalAccount.postValue(it.toString())
            }
            editTextEmailAuthNumber.addTextChangedListener {
                binding.buttonFinishEmailAuth.isEnabled = it?.isNotEmpty() ?: false
            }
            buttonSendAuthNumber.setOnClickListener {
                emailAuthenticationFragmentViewModel.sendAuthNumber(
                        portalAccount = "${editTextEmail.text}@koreatech.ac.kr",
                        onSuccess = {
                            if (emailAuthenticationFragmentViewModel.sentEmailAuth.value == true)
                                showResentEmailAuthNumberDialog()
                        },
                        onError = {
                            showEmailAuthFailedDialog()
                        }
                )
            }
            buttonFinishEmailAuth.setOnClickListener {
                emailAuthenticationFragmentViewModel.finishEmailAuth(
                        portalAccount = "${editTextEmail.text}@koreatech.ac.kr",
                        secret = editTextEmailAuthNumber.text.toString(),
                        onSuccess = { nextPage() },
                        onError = { showEmailAuthFailedDialog() }
                )
            }
        }
    }

    private fun nextPage() {
        (activity as ChangePasswordActivity).nextPage()
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