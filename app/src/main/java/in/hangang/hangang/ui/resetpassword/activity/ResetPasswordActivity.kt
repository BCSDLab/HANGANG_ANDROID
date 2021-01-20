package `in`.hangang.hangang.ui.resetpassword.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityFindPasswordBinding
import `in`.hangang.hangang.ui.resetpassword.adapter.NewPasswordPagerAdapter
import android.os.Bundle

class ResetPasswordActivity :
    ViewBindingActivity<ActivityFindPasswordBinding>(R.layout.activity_find_password) {

    val newPasswordPagerAdapter: NewPasswordPagerAdapter by lazy {
        NewPasswordPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun nextPage() {
        binding.viewPager.currentItem += 1
    }
}