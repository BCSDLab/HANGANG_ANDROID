package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.button.RoundedCornerButton.Companion.OUTLINED
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpEmailBinding
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpEmailActivity : ViewBindingActivity<ActivitySignUpEmailBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_email
    private var isAuthNumSend = false // 한번이라도 인증번호 전송을 눌렀는지 확인
    private var isAutnNumable = false // 인증번호 전송활성화 여부
    private val signUpEmailViewModel: SignUpEmailViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = signUpEmailViewModel
        init()
    }

    private fun init() {
        initAppBar()
        initEmailEditText()
        initAuthnumEditText()
        initEvent()
        handleObserver()
    }

    private fun handleObserver() {
        signUpEmailViewModel.emailConfigSendText.observe(this, {
            if (it.equals("OK")) {
                var intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra("id", binding.emailEditText.text.toString())
                startActivity(intent)
            } else {
                initErrorDialog()
            }
        })
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initEvent() {
        binding.authnumSendButton.setOnClickListener {
            if (isAutnNumable) {
                if (isAuthNumSend == false) {
                    signUpEmailViewModel.sendEmail(
                        binding.emailEditText.text.toString().plus("@koreatech.ac.kr")
                    )
                    resendAvailable()
                    isAuthNumSend = true;
                } else {
                    signUpEmailViewModel.sendEmail(
                        binding.emailEditText.text.toString().plus("@koreatech.ac.kr")
                    )
                    initResendDialog()
                }
            }
        }

        binding.authCompleteButton.setOnClickListener {
            if (binding.authCompleteButton.isEnabled)
                signUpEmailViewModel.sendEmailConfig(
                    binding.emailEditText.text.toString().plus("@koreatech.ac.kr"),
                    binding.authnumEditText.text.toString()
                )
        }
    }

    private fun initEmailEditText() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0 && isAuthNumSend == false) {
                    authNumAvailable()
                    isAutnNumable = true
                } else if (s?.length!! > 0 && isAuthNumSend) {
                    resendAvailable()
                    isAutnNumable = true
                } else if ((s?.length!! <= 0 && isAuthNumSend == false)) {
                    authNumDisavailable()
                    isAutnNumable = false
                } else {
                    resendDisavaiable()
                    isAutnNumable = false
                }
            }
        })
    }

    private fun initAuthnumEditText() {
        binding.authnumEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    binding.authCompleteButton.isEnabled = true
                } else {
                    binding.authCompleteButton.isEnabled = false
                }
            }
        })
    }

    private fun initErrorDialog() {
        var dialogListener = View.OnClickListener {
            binding.authnumEditText.setText("")
            authNumAvailable()
            isAuthNumSend = false
            dialog?.dismiss()

        }
        showSimpleDialog(
            title = getString(R.string.reset_password_error_auth),
            message = getString(R.string.check_authnum_or_email),
            positiveButtonText = getString(R.string.reset_password_retry_auth),
            positiveButtonOnClickListener = dialogListener,
            cancelable = false
        )

    }

    private fun initResendDialog() {

        showSimpleDialog(
            title = getString(R.string.reset_password_dialog_resent_title),
            message = getString(R.string.reset_password_dialog_check_portal_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dismissSimpleDialog() },
            cancelable = false
        )

    }

    private fun authNumAvailable() {
        binding.authnumSendButton.isEnabled = true
        binding.authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
    }

    private fun authNumDisavailable() {
        binding.authnumSendButton.isEnabled = false
        binding.authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
    }

    private fun resendDisavaiable() {
        binding.authnumSendButton.appearence = OUTLINED
        binding.authnumSendButton.isEnabled = false
        binding.authnumSendButton.setTextColor(
            ContextCompat.getColor(
                baseContext,
                `in`.hangang.core.R.color.blue_100
            )
        )
        binding.authnumSendButton.text = getString(R.string.reset_password_resend_auth_number)
    }

    private fun resendAvailable() {
        binding.authnumSendButton.appearence = OUTLINED
        binding.authnumSendButton.isEnabled = true
        binding.authnumSendButton.text = getString(R.string.reset_password_resend_auth_number)
    }
}