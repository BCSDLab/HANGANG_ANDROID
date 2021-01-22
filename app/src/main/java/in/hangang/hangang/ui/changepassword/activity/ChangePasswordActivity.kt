package `in`.hangang.hangang.ui.changepassword.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityFindPasswordBinding
import `in`.hangang.hangang.ui.changepassword.adapter.ChangePasswordPagerAdapter
import android.os.Bundle

class ChangePasswordActivity : ViewBindingActivity<ActivityFindPasswordBinding>() {
    override val layoutId = R.layout.activity_find_password

    val changePasswordPagerAdapter: ChangePasswordPagerAdapter by lazy {
        ChangePasswordPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewPager.adapter = changePasswordPagerAdapter
    }

    fun nextPage() {
        binding.viewPager.currentItem += 1
    }

}