package `in`.hangang.hangang.ui.signup

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.appbar.ProgressAppBar
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpEmailBinding
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpEmailActivity : ViewBindingActivity<ActivitySignUpEmailBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_email
    private var isAuthNumSend = false // 한번이라도 인증번호 전송을 눌렀는지 확인
    private var isAutnNumable = false // 인증번호 전송활성화 여부
    private var isConfigable = false
    private val signUpEmailViewModel: SignUpEmailViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = signUpEmailViewModel
        init()

        binding.authnumSendButton.setOnClickListener {
            if (isAutnNumable) {
                if (isAuthNumSend == false) {
                    //resendAvailable()
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
                    dialog?.show()
                }
            }
        }


        binding.authCompleteButton.setOnClickListener {
            if (isConfigable)
                signUpEmailViewModel.sendEmailConfig(
                    binding.emailEditText.text.toString().plus("@koreatech.ac.kr"),
                    binding.authnumEditText.text.toString()
                )
        }

        signUpEmailViewModel.emailConfigSendText.observe(this, {
            if (it.equals("OK")) {
                var intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            } else {
                initErrorDialog()
                dialog?.show()
            }
        })


    }

    fun init() {

        initAppBar()
        initEmailEditText()
        initAuthnumEditText()
    }

    fun initAppBar() {
        binding.appBar.title = "회원가입"
    }

    fun initEmailEditText() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

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
    fun initAuthnumEditText(){
        binding.authnumEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    binding.authCompleteButton.background =
                        getDrawable(R.drawable.rectangle_rounded_corner_blue_500)
                    isConfigable = true
                } else {
                    binding.authCompleteButton.background =
                        getDrawable(R.drawable.rectangle_rounded_corner_blue_100)
                    isConfigable = false
                }
            }
        })
    }
    fun initErrorDialog(){
        var dialogListener = View.OnClickListener {
            binding.authnumEditText.setText("")
            authNumAvailable()
            isConfigable = false
            isAuthNumSend = false
            dialog?.dismiss()
        }
        dialog = DialogUtil.makeSimpleDialog(
            this,
            "인증 오류",
            "인증번호나 아우누리 메일을 다시 확인하여 주세요.\n이메일 인증 완료 후 서비스를 이용할 수 있습니다.",
            "다시 인증하기",
            null,
            dialogListener,
            null,
            true
        )
    }
    fun initResendDialog() {
        var dialogListener = View.OnClickListener {
            dialog?.dismiss()
            LogUtil.e("click")
        }
        dialog = DialogUtil.makeSimpleDialog(
            this,
            "재전송 되었습니다",
            "아우누리 메일을 확인해주세요.\n이메일 인증 완료 후 서비스를 이용할 수 있습니다.",
            "확인",
            null,
            dialogListener,
            null,
            false
        )
    }
    fun authNumAvailable(){
        binding.authnumSendButton.background =
            getDrawable(R.drawable.rectangle_rounded_corner_blue_500)
        binding.authnumSendButton.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.white
            )
        )
        binding.authnumSendButton.text = "인증번호 전송"
    }
    fun authNumDisavailable(){
        binding.authnumSendButton.background =
            getDrawable(R.drawable.rectangle_rounded_corner_blue_100)
        binding.authnumSendButton.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.white
            )
        )
        binding.authnumSendButton.text = "인증번호 전송"
    }
    fun resendDisavaiable(){
        binding.authnumSendButton.background =
            getDrawable(R.drawable.rectangle_rounded_corner_outline_blue_100)
        binding.authnumSendButton.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.blue_100
            )
        )
        binding.authnumSendButton.text = "재전송"
    }
    fun resendAvailable(){
        binding.authnumSendButton.background =
            getDrawable(R.drawable.rectangle_rounded_corner_outline_blue_500)
        binding.authnumSendButton.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.blue_500
            )
        )
        binding.authnumSendButton.text = "재전송"
    }
}