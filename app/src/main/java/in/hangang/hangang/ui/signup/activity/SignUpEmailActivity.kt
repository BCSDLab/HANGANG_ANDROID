package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.WebViewActivity
import `in`.hangang.core.base.activity.getColorFromAttr
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.view.button.RoundedCornerButton.Companion.FILLED
import `in`.hangang.core.view.button.RoundedCornerButton.Companion.OUTLINED
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.ERROR
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.UNDEFINED
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpEmailBinding
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpEmailActivity : ViewBindingActivity<ActivitySignUpEmailBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_email
    private val PORTAL_URL = "https://portal.koreatech.ac.kr/login.jsp"
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
            if (it.httpStatus == "OK") {
                binding.authnumEditText.status = UNDEFINED
                var intent = Intent(this, SignUpActivity::class.java).run {
                    putExtra("id", binding.emailEditText.text.toString())
                    startActivity(this)
                }
            } else {
                binding.signupEmailConfigErrorTextview.text = getString(R.string.sign_up_email_config_error_message)
                binding.authnumEditText.status = ERROR
                //initCommonErrorDialog(it)
            }
        })

        signUpEmailViewModel.sentEmailAuth.observe(this, {
            when(it) {
                SignUpEmailViewModel.AuthEmailState.UNACTIVE -> {
                    binding.authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
                    binding.authnumSendButton.appearence = FILLED
                    binding.authnumSendButton.isEnabled = false
                    binding.authnumSendButton.setTextColor(getColorFromAttr(R.attr.colorOnPrimary))
                    binding.authnumEditText.isEditTextEnabled = false
                }
                SignUpEmailViewModel.AuthEmailState.ACTIVE -> {
                    binding.authnumSendButton.text = getString(R.string.reset_password_send_auth_number)
                    binding.authnumSendButton.appearence = FILLED
                    binding.authnumSendButton.isEnabled = true
                    binding.authnumSendButton.setTextColor(getColorFromAttr(R.attr.colorOnPrimary))
                    binding.authnumEditText.isEditTextEnabled = false
                }
                SignUpEmailViewModel.AuthEmailState.RETRY -> {
                    binding.authnumSendButton.text = getString(R.string.reset_password_resend_auth_number)
                    binding.authnumSendButton.appearence = OUTLINED
                    binding.authnumSendButton.setTextColor(ContextCompat.getColor(baseContext, `in`.hangang.core.R.color.blue_200))
                    binding.authnumEditText.isEditTextEnabled = true
                    binding.goToAunuriButton.visibility = View.VISIBLE
                }
            }
        })
        signUpEmailViewModel.emailSendText.observe(this, {
            if(it == 14){
                binding.signupEmailErrorTextview.text = getString(R.string.sign_up_email_duplicated_error_message)
            } else if(it == 22){
                initCommonErrorDialog(getString(R.string.sign_up_email_five_limit_title), getString(R.string.sign_up_email_five_limit_message))
            } else {
                initErrorDialog()
            }

        })
        signUpEmailViewModel.showAlert.observe(this, {
            if(it){
                initResendDialog()
            }
        })
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initEvent() {
        with(binding) {
            authnumSendButton.setOnClickListener {
                binding.signupEmailErrorTextview.text = ""
                if(signUpEmailViewModel.sentEmailAuth.value == SignUpEmailViewModel.AuthEmailState.ACTIVE ) {
                    signUpEmailViewModel.sentEmailAuth.value = SignUpEmailViewModel.AuthEmailState.RETRY
                    signUpEmailViewModel.sendEmail(
                        binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech))
                    )
                } else if(signUpEmailViewModel.sentEmailAuth.value == SignUpEmailViewModel.AuthEmailState.RETRY ) {
                    signUpEmailViewModel.sendEmail(
                        binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech))
                    )
                }

            }
            authCompleteButton.setOnClickListener {
                if (binding.authCompleteButton.isEnabled)
                    signUpEmailViewModel.sendEmailConfig(
                            binding.emailEditText.text.toString().plus(getString(R.string.email_koreatech)),
                            binding.authnumEditText.text.toString()
                    )
            }
            goToAunuriButton.isEnabled = true
            goToAunuriButton.setOnClickListener {
                val intent = Intent(this@SignUpEmailActivity, WebViewActivity::class.java)
                intent.putExtra("url", PORTAL_URL)
                startActivity(intent)
            }
        }


    }

    private fun initEmailEditText() {
        with(binding) {
            emailEditText.addTextChangedListener {
                if (it?.length!! > 0 ) {
                    signUpEmailViewModel.sentEmailAuth.value = SignUpEmailViewModel.AuthEmailState.ACTIVE
                } else {
                    signUpEmailViewModel.sentEmailAuth.value = SignUpEmailViewModel.AuthEmailState.UNACTIVE
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
    private fun initCommonErrorDialog(title: String,errorMessage: String) {
        showSimpleDialog(
            title = title,
            message = errorMessage,
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog?.dismiss()
            },
            cancelable = false
        )
    }
    private fun initErrorDialog() {
        showSimpleDialog(
                title = getString(R.string.reset_password_error_auth),
                message = getString(R.string.check_authnum_or_email),
                positiveButtonText = getString(R.string.reset_password_retry_auth),
                positiveButtonOnClickListener = { dialog, _ ->
                    binding.authnumEditText.setText("")
                    //authNumAvailable()
                    //signUpEmailViewModel.isAuthNumSend = false
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
                    signUpEmailViewModel.showAlert.value = false
                },
                cancelable = false
        )

    }

}