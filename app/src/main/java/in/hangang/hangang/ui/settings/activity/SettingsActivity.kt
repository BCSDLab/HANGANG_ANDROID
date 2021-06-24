package `in`.hangang.hangang.ui.settings.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySettingsBinding
import `in`.hangang.hangang.ui.login.LoginActivity
import `in`.hangang.hangang.ui.settings.viewmodel.SettingsActivityViewModel
import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ViewBindingActivity<ActivitySettingsBinding>() {
    override val layoutId: Int = R.layout.activity_settings
    private val settingsViewModel: SettingsActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = settingsViewModel

        initView()
        initViewModel()
        initEvent()
    }

    private fun initView() {
    }

    private fun initEvent() {
        with(binding) {
            changeProfileButton.setOnClickListener {
                myProfileActivityStart()
            }
            logOutButton.setOnClickListener {
                showLogoutDialog()
            }
            deleteAccountButton.setOnClickListener {
                showDeleteAccountDialog()
            }
        }


    }


    private fun initViewModel() {
        with(settingsViewModel) {
            autoLoginButtonResponse.observe(this@SettingsActivity){
                if(it){
                    saveAutoLoginStatus(true)
                }
                else{
                    saveAutoLoginStatus(false)
                }
            }

        }
    }

    private fun myProfileActivityStart() {
        Intent(this, MyProfileActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun showLogoutDialog() {
        showSimpleDialog(
            message = getString(R.string.profile_logout_dialog_message),
            positiveButtonText = getString(R.string.ok),
            negativeButtonText = getString(R.string.close),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
                settingsViewModel.logoutAll()
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
            },
            negativeButtonOnClickListener = {dialog, _ ->
                dialog.dismiss()
            }

        )
    }

    private fun showDeleteAccountDialog() {
        showSimpleDialog(
            message = getString(R.string.profile_delete_account_dialog_message),
            positiveButtonText = getString(R.string.ok),
            negativeButtonText = getString(R.string.close),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
                    settingsViewModel.deleteAccount()
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            },
            negativeButtonOnClickListener = {dialog, _ ->
                dialog.dismiss()
            }
        )
    }


}