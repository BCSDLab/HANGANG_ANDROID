package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpDocumentBinding
import android.content.Intent
import android.os.Bundle

class SignUpDocumentActivity : ViewBindingActivity<ActivitySignUpDocumentBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_document
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initAppBar()
        initCheckBox()
        initEvent()
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initCheckBox() {
        with(binding){
            agreeAllCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                } else {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                }
            }
            agreeHangangService.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                } else {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                }
            }
            agreePersonalInfo.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                } else {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                }
            }
        }
    }
    private fun initEvent(){
        binding.nextButton.setOnClickListener {
            if (binding.nextButton.isEnabled) {
                var intent = Intent(this, SignUpEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isCheckBoxChecked(): Boolean {
        if (binding.agreePersonalInfo.isChecked && binding.agreeAllCheckbox.isChecked && binding.agreeHangangService.isChecked) {
            return true
        } else {
            return false
        }
    }
}