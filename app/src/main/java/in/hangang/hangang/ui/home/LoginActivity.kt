package `in`.hangang.hangang.ui.home

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityLoginBinding
import `in`.hangang.hangang.ui.changepassword.activity.ChangePasswordActivity
import `in`.hangang.hangang.ui.signup.activity.SignUpDocumentActivity
import android.content.Intent
import android.os.Bundle

class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {
    override val layoutId: Int = R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.joinInButton.setOnClickListener {
            val intent = Intent(this, SignUpDocumentActivity::class.java)
            startActivity(intent)
        }
        binding.searchPasswordButton.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }
}