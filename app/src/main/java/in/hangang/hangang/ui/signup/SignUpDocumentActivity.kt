package `in`.hangang.hangang.ui.signup

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.ProgressAppBar
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpDocumentBinding
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpDocumentActivity : ViewBindingActivity<ActivitySignUpDocumentBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_document
    private var isNextStep = false
    private val viewmodel: SignUpViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

    }
    fun init(){

        initAppBar()
        initCheckBox()
    }
    fun initAppBar(){
        binding.appBar.title = "회원가입"
    }
    fun initCheckBox() {

        binding.agreeAllCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxCheck()
            } else {
                isNextStep = false
                binding.nextButton.background =
                    getDrawable(R.drawable.rectangle_rounded_corner_blue_100)
            }
        }
        binding.agreeHangangService.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxCheck()
            } else {
                binding.nextButton.background =
                    getDrawable(R.drawable.rectangle_rounded_corner_blue_100)
                isNextStep = false
            }
        }
        binding.agreePersonalInfo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxCheck()
            } else {
                binding.nextButton.background =
                    getDrawable(R.drawable.rectangle_rounded_corner_blue_100)
                isNextStep = false
            }
        }
        binding.nextButton.setOnClickListener {
            LogUtil.e(isNextStep.toString())
            if (isNextStep) {
                var intent = Intent(this, SignUpEmailActivity::class.java)
                startActivity(intent)
            }
        }


    }
    fun checkBoxCheck() {
        if (binding.agreePersonalInfo.isChecked && binding.agreeAllCheckbox.isChecked && binding.agreeHangangService.isChecked) {
            isNextStep = true
            binding.nextButton.background =
                getDrawable(R.drawable.rectangle_rounded_corner_blue_500)
        } else {
            isNextStep = false
        }
    }
}