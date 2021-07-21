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
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {
    override val layoutId = R.layout.activity_login

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.let {
            it.vm = loginViewModel
        }

        handleObserver()
        initEvent()
    }

    private fun initEvent() {
        with(binding) {
            loginButton.setOnClickListener {
                loginViewModel.loginButtonClick(
                    portalID = portalId.text.toString().plus(getString(R.string.email_koreatech)),
                    password = portalPassword.text.toString().toSHA256()
                )
            }
            findPassword.setOnClickListener {
                val intent = Intent(this@LoginActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            signIn.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpDocumentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun handleObserver() {
        with(loginViewModel) {
            isLoginSuccess.observe(this@LoginActivity) {
                if (it) {
                    saveAutoLoginStatus(true)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
            errorConfig.observe(this@LoginActivity, { status ->
                binding.loginErrorText.isVisible = status

            })
            isLoading.observe(this@LoginActivity, {
                when (it) {
                    true -> showProgressDialog()
                    false -> hideProgressDialog()
                }
            })
        }

    }
}