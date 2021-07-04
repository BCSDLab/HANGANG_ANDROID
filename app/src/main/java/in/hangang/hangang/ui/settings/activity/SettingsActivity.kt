package `in`.hangang.hangang.ui.settings.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySettingsBinding
import `in`.hangang.hangang.ui.login.LoginActivity
import `in`.hangang.hangang.ui.settings.viewmodel.SettingsActivityViewModel
import android.content.Intent
import android.os.Bundle
import android.util.EventLog
import com.orhanobut.hawk.Hawk
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
        settingsViewModel.getAutoLoginStatus()
    }

    private fun initEvent() {
        with(binding) {
            logOutButton.setOnClickListener {
                showLogoutDialog()
            }
            changeProfileButton.setOnClickListener {
                myProfileActivityStart()
            }
            deleteAccountButton.setOnClickListener {
                showDeleteAccountDialog()
            }
            autoLoginToggleButton.setOnClickListener {
                settingsViewModel.saveAutoLoginStatus(autoLoginToggleButton.isChecked)
            }


        }


    }


    private fun initViewModel() {
        with(settingsViewModel) {
            autoLoginButtonResponse.observe(this@SettingsActivity){
                binding.autoLoginToggleButton.isChecked = it
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
                    startActivity(this)
                }
            },
            negativeButtonOnClickListener = {dialog, _ ->
                dialog.dismiss()
            }
        )
    }


}