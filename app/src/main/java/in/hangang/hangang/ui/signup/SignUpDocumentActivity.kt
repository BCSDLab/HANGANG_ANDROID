package `in`.hangang.hangang.ui.signup

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
        binding.agreeAllCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.nextButton.isEnabled = isCheckBoxCheck()
            } else {
                binding.nextButton.isEnabled = isCheckBoxCheck()
            }
        }
        binding.agreeHangangService.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.nextButton.isEnabled = isCheckBoxCheck()
            } else {
                binding.nextButton.isEnabled = isCheckBoxCheck()
            }
        }
        binding.agreePersonalInfo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.nextButton.isEnabled = isCheckBoxCheck()
            } else {
                binding.nextButton.isEnabled = isCheckBoxCheck()
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

    private fun isCheckBoxCheck(): Boolean {
        if (binding.agreePersonalInfo.isChecked && binding.agreeAllCheckbox.isChecked && binding.agreeHangangService.isChecked) {
            return true
        } else {
            return false
        }
    }
}