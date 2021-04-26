package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.view.button.RoundedCornerButton.Companion.FILLED
import `in`.hangang.core.view.button.RoundedCornerButton.Companion.OUTLINED
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpEmailBinding
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpEmailActivity : ViewBindingActivity<ActivitySignUpEmailBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_email
    private val signUpEmailViewModel: SignUpEmailViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = signUpEmailViewModel
        init()
    }

    private fun init() {
        initAppBar()
        initEmailEditText()
        initAuthNumEditText()
        initEvent()
        handleObserver()
    }

    private fun handleObserver() {
        signUpEmailViewModel.emailConfigSendText.observe(this, {
            if (it) {
                var intent = Intent(this, SignUpActivity::class.java).run {
                    putExtra("id", binding.emailEditText.text.toString())
                    startActivity(this)
                }
            }
        })
        signUpEmailViewModel.errorConfig.observe(this, {
            if (it) {
                initErrorDialog()
            }
        })
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initEvent() {
        with(binding) {
            authnumSendButton.setOnClickListener {
                if (signUpEmailViewModel.isAuthNumable) {
                    if (signUpEmailViewModel.isAuthNumSend == false) {
                        signUpEmailViewModel.sendEmail(
                            binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech))
                        )
                        resendAvailable()
                        signUpEmailViewModel.isAuthNumSend = true
                    } else {
                        signUpEmailViewModel.sendEmail(
                            binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech))
                        )
                        initResendDialog()
                    }
                }
            }
            authCompleteButton.setOnClickListener {
                if (binding.authCompleteButton.isEnabled)
                    signUpEmailViewModel.sendEmailConfig(
                        binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech)),
                        binding.authnumEditText.text.toString()
                    )
            }
        }


    }

    private fun initEmailEditText() {
        with(binding) {
            emailEditText.addTextChangedListener {
                if (it?.length!! > 0 && signUpEmailViewModel.isAuthNumSend == false) {
                    authNumAvailable()
                    signUpEmailViewModel.isAuthNumable = true
                } else if (it.length > 0 && signUpEmailViewModel.isAuthNumSend) {
                    resendAvailable()
                    signUpEmailViewModel.isAuthNumable = true
                } else if ((it.length <= 0 && signUpEmailViewModel.isAuthNumSend == false)) {
                    authNumDisavailable()
                    signUpEmailViewModel.isAuthNumable = false
                } else {
                    resendDisavaiable()
                    signUpEmailViewModel.isAuthNumable = false
                }
            }
        }

    }

    private fun initAuthNumEditText() {
        with(binding) {
            authnumEditText.addTextChangedListener {
                binding.authCompleteButton.isEnabled = it?.length!! > 0
            }
        }
    }

    private fun initErrorDialog() {
        showSimpleDialog(
            title = getString(R.string.reset_password_error_auth),
            message = getString(R.string.check_authnum_or_email),
            positiveButtonText = getString(R.string.reset_password_retry_auth),
            positiveButtonOnClickListener = { dialog, _ ->
                binding.authnumEditText.setText("")
                authNumAvailable()
                signUpEmailViewModel.isAuthNumSend = false
                dialog?.dismiss()

            },
            cancelable = false
        )
    }

    private fun initResendDialog() {
        showSimpleDialog(
            title = getString(R.string.reset_password_dialog_resent_title),
            message = getString(R.string.reset_password_dialog_check_portal_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            },
            cancelable = false
        )

    }

    private fun authNumAvailable() {
        with(binding) {
            authnumSendButton.isEnabled = true
            authnumSendButton.appearence = FILLED
            authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
        }
    }

    private fun authNumDisavailable() {
        with(binding) {
            authnumSendButton.isEnabled = false
            authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
        }
    }

    private fun resendDisavaiable() {
        with(binding) {
            authnumSendButton.appearence = OUTLINED
            authnumSendButton.isEnabled = false
            authnumSendButton.setTextColor(ContextCompat.getColor(baseContext, `in`.hangang.core.R.color.blue_100))
            authnumSendButton.text = getString(R.string.reset_password_resend_auth_number)
        }
    }

    private fun resendAvailable() {
        with(binding) {
            authnumSendButton.appearence = OUTLINED
            authnumSendButton.isEnabled = true
            authnumSendButton.text = getString(R.string.reset_password_resend_auth_number)
        }
    }
}