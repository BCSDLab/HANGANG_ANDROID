package `in`.hangang.hangang.ui.login

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityLoginBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {
    override val layoutId = R.layout.activity_login

    private val loginViewModel : LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initEvent()
        initViewModel()
        }

    private fun initEvent(){
        with(binding){
            loginButton.setOnClickListener {
                loginViewModel.loginButtonClick(
                    portalID = portalId.text.toString(),
                    password = portalPassword.text.toString()
                )
            }
            findPassword.setOnClickListener{
                val intent = Intent(this@LoginActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
//            signIn.setOnClickListener {
//                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
//                startActivity(intent)
//            }
        }
    }


    private fun initViewModel(){
        with(loginViewModel){
            loginButtonClickResponse.observe(this@LoginActivity){
            }
            findPasswordClickResponse.observe(this@LoginActivity){
            }
            signInClickResponse.observe(this@LoginActivity){
            }
        }

    }
}