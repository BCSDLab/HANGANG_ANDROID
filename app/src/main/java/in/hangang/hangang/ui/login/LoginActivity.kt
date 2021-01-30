package `in`.hangang.hangang.ui.login

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.util.toSHA256
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityLoginBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import `in`.hangang.hangang.ui.main.MainActivity
import `in`.hangang.hangang.ui.signup.activity.SignUpDocumentActivity
import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {
    override val layoutId = R.layout.activity_login

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEvent()
        initViewModel()
    }

    private fun initEvent() {
        with(binding) {
            loginButton.setOnClickListener {
                loginViewModel.loginButtonClick(
                    portalID = (portalId.text.toString()+"@koreatech.ac.kr"),
                    password = portalPassword.text.toString().toSHA256()
                )
                var intent= Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            findPassword.setOnClickListener {
                var intent = Intent(this@LoginActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            signIn.setOnClickListener {
                var intent = Intent(this@LoginActivity, SignUpDocumentActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun initViewModel() {
        with(loginViewModel) {
            loginButtonClickResponse.observe(this@LoginActivity) {
            }
        }

    }
}