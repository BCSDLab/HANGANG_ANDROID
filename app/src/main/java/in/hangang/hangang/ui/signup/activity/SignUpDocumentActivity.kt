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
        with(binding) {
            agreeAllTextView.setOnClickListener {
                agreeAllCheckbox.performClick()
            }
            agreeAllCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                } else {
                    binding.nextButton.isEnabled = isCheckBoxChecked()
                }
            }

            agreePersonalInfoFirstTextView.setOnClickListener {
                agreePersonalInfo.performClick()
            }

            agreePersonalInfoSecondTextView.setOnClickListener {
                agreePersonalInfo.performClick()
            }

            agreePersonalInfoThirdTextView.setOnClickListener {
                agreePersonalInfo.performClick()
            }

            agreePersonalInfoFourthTextView.setOnClickListener {
                agreePersonalInfo.performClick()
            }

            binding.agreePersonalInfoFifthTextView.setOnClickListener {
                agreePersonalInfo.performClick()
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

            communityRuleFirstTextView.setOnClickListener {
                agreeHangangService.performClick()
            }
            communityRuleSecondTextView.setOnClickListener {
                agreeHangangService.performClick()
            }
            communityRuleThirdTextView.setOnClickListener {
                agreeHangangService.performClick()
            }
            communityRuleFourthTextView.setOnClickListener {
                agreeHangangService.performClick()
            }
            communityRuleFifthTextView.setOnClickListener {
                agreeHangangService.performClick()
            }
        }
    }

    private fun initEvent() {
        binding.nextButton.setOnClickListener {
            if (binding.nextButton.isEnabled) {
                var intent = Intent(this, SignUpEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isCheckBoxChecked(): Boolean {
        return binding.agreePersonalInfo.isChecked && binding.agreeAllCheckbox.isChecked && binding.agreeHangangService.isChecked
    }
}
