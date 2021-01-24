package `in`.hangang.hangang.ui.changepassword.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityFindPasswordBinding
import `in`.hangang.hangang.ui.changepassword.adapter.ChangePasswordPagerAdapter
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : ViewBindingActivity<ActivityFindPasswordBinding>() {

    private val changePasswordActivityViewModel: ChangePasswordActivityViewModel by viewModel()
    private val changePasswordFragmentViewModel: ChangePasswordFragmentViewModel by viewModel()
    private val emailAuthenticationFragmentViewModel: EmailAuthenticationFragmentViewModel by viewModel()

    override val layoutId = R.layout.activity_find_password
    private val changePasswordPagerAdapter: ChangePasswordPagerAdapter by lazy {
        ChangePasswordPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        changePasswordActivityViewModel.currentPage.observe(this) {
            binding.viewPager.currentItem = it
            binding.appBar.progress = it
        }
    }

    private fun initView() {
        with(binding) {
            viewPager.isUserInputEnabled = false
            viewPager.adapter = changePasswordPagerAdapter

            appBar.max = changePasswordPagerAdapter.itemCount
        }
    }

    fun nextPage() {
        changePasswordActivityViewModel.currentPage.postValue(1)
    }

    fun finishChangePassword() {
        changePasswordActivityViewModel.currentPage.postValue(2)
    }

}