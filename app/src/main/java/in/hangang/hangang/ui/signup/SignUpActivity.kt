package `in`.hangang.hangang.ui.signup

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.ERR_NO_INPUT
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpBinding
import `in`.hangang.hangang.databinding.ActivitySignUpEmailBinding
import `in`.hangang.hangang.util.LogUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignUpActivity  : ViewBindingActivity<ActivitySignUpBinding>()  {
    override val layoutId: Int = R.layout.activity_sign_up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }
}