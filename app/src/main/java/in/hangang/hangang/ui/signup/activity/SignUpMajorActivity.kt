package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpMajorBinding
import `in`.hangang.hangang.ui.login.LoginActivity
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpMajorViewModel
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SignUpMajorActivity : ViewBindingActivity<ActivitySignUpMajorBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_major
    private val signUpMajorViewModel: SignUpMajorViewModel by viewModel {
        parametersOf(
                intent.getStringExtra("portalAccount"),
                intent.getStringExtra("nickName"),
                intent.getStringExtra("password")
        )
    }


    private var portalAccount: String? = null
    private var nickName: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun showSignUpCompleteDialog() {
        showSimpleDialog(
                title = getString(R.string.sign_up_complete_dialog_title),
                message = String.format(getString(R.string.sign_up_complete_dialog_message), nickName),
                positiveButtonText = getString(R.string.reset_password_finished_positive_button),
                positiveButtonOnClickListener = { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                },
                cancelable = false
        )
    }

    private fun init() {
        portalAccount = signUpMajorViewModel.portalAccount.plus(getString(R.string.email_koreatech))
        nickName = signUpMajorViewModel.nickName
        password = signUpMajorViewModel.password
        initAppBar()
        initCheckBox()
        initEditText()
        initEvent()
        handleObserver()

    }

    private fun initEditText() {
        signUpMajorViewModel.signUpMessage.observe(this, {
            if (it.equals(getString(R.string.en_ok))) {
                showSignUpCompleteDialog()
            }
        })
    }

    private fun handleObserver() {
        signUpMajorViewModel.signUpMessage.observe(this, {
            if (it.equals(getString(R.string.en_ok))) {
                showSignUpCompleteDialog()
            }
        })
    }

    private fun initEvent() {
        binding.majorCompleteButton.setOnClickListener {
            if (binding.majorCompleteButton.isEnabled) {
                signUpMajorViewModel.major =
                        ArrayList(signUpMajorViewModel.majorHashMap.values).toArray(arrayOfNulls(signUpMajorViewModel.majorHashMap.values.size))
                signUpMajorViewModel.signUp(signUpMajorViewModel.major, nickName!!, password!!, portalAccount!!)
            }
        }
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initCheckBox() {
        with(binding) {
            mechanicalEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(1, getString(R.string.mechanical_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(1)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            designEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(2, getString(R.string.design_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(2)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            mechatronicsEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(3, getString(R.string.mechatronics_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(3)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            industryManagementCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(4, getString(R.string.industrial_management))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(4)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            energyEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(5, getString(R.string.energy_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(5)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            electronicEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(6, getString(R.string.electronic_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(6)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
            computerEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    signUpMajorViewModel.majorHashMap.put(7, getString(R.string.computer_engineering))
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                } else {
                    signUpMajorViewModel.majorHashMap.remove(7)
                    binding.majorCompleteButton.isEnabled = checkBoxCheck()
                }
            }
        }

    }

    private fun checkBoxCheck(): Boolean {
        with(binding) {
            return mechanicalEngineeringCheckbox.isChecked || designEngineeringCheckbox.isChecked || mechatronicsEngineeringCheckbox.isChecked ||
                    industryManagementCheckbox.isChecked || energyEngineeringCheckbox.isChecked || electronicEngineeringCheckbox.isChecked ||
                    computerEngineeringCheckbox.isChecked
        }
    }

}