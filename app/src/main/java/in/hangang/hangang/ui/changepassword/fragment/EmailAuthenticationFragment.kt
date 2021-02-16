package `in`.hangang.hangang.ui.changepassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.base.fragment.getColorFromAttr
import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.core.view.edittext.EditTextWithError
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmailAuthenticationFragment : ViewBindingFragment<FragmentEmailAuthenticationBinding>() {
    override val layoutId = R.layout.fragment_email_authentication

    private val changePasswordActivityViewModel: ChangePasswordActivityViewModel by sharedViewModel()
    private val emailAuthenticationFragmentViewModel: EmailAuthenticationFragmentViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = emailAuthenticationFragmentViewModel

        initEvent()
        initViewModel()
    }

    private fun initViewModel() {
        with(emailAuthenticationFragmentViewModel) {
            sentEmailAuth.observe(viewLifecycleOwner) {
                if (it) {
                    binding.buttonSendAuthNumber.appearence = RoundedCornerButton.OUTLINED
                    binding.buttonSendAuthNumber.setTextColor(getColorFromAttr(R.attr.colorOnSurface))
                } else {
                    binding.buttonSendAuthNumber.appearence = RoundedCornerButton.FILLED
                    binding.buttonSendAuthNumber.setTextColor(getColorFromAttr(R.attr.colorOnPrimary))
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
            emailErrorMessage.observe(viewLifecycleOwner) {
                binding.textViewEmailErrorMessage.text = it ?: getString(R.string.unknown_error)
                binding.editTextEmail.status =
                    if (it == null || it.isNotEmpty()) EditTextWithError.ERROR else EditTextWithError.UNDEFINED
            }
            emailAuthNumberErrorMessage.observe(viewLifecycleOwner) {
                binding.textViewEmailAuthNumberErrorMessage.text =
                    it ?: getString(R.string.unknown_error)
                binding.editTextEmailAuthNumber.status =
                    if (it == null || it.isNotEmpty()) EditTextWithError.ERROR else EditTextWithError.UNDEFINED
            }
        }
    }

    private fun initEvent() {
        binding.buttonSendAuthNumber.isEnabled = false
        binding.buttonFinishEmailAuth.isEnabled = false
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
            buttonGotoPortal.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.koreatech.ac.kr/")))
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