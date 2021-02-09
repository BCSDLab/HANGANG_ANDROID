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

    val changePasswordActivityViewModel: ChangePasswordActivityViewModel by viewModel()
    val changePasswordFragmentViewModel: ChangePasswordFragmentViewModel by viewModel()
    val emailAuthenticationFragmentViewModel: EmailAuthenticationFragmentViewModel by viewModel()

    override val layoutId = R.layout.activity_find_password

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

        changePasswordActivityViewModel.isLoading.observe(this) {
            if (it) showProgressDialog()
            else hideProgressDialog()
        }
        emailAuthenticationFragmentViewModel.isLoading.observe(this) {
            changePasswordActivityViewModel.isLoading.postValue(
                it || changePasswordFragmentViewModel.isLoading.value == true
            )
        }
        changePasswordFragmentViewModel.isLoading.observe(this) {
            changePasswordActivityViewModel.isLoading.postValue(
                it || emailAuthenticationFragmentViewModel.isLoading.value == true
            )
        }
    }

    private fun initView() {
        with(binding) {
            viewPager.isUserInputEnabled = false
            viewPager.adapter = ChangePasswordPagerAdapter(this@ChangePasswordActivity)

            appBar.max = viewPager.adapter?.itemCount ?: 0
        }
    }
}