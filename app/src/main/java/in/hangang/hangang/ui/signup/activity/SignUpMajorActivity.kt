package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpMajorBinding
import `in`.hangang.hangang.ui.home.LoginActivity
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpMajorViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpMajorActivity : ViewBindingActivity<ActivitySignUpMajorBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up_major
    private val signUpMajorViewModel: SignUpMajorViewModel by viewModel()
    private var major = emptyArray<String>()
    private var majorHashMap: HashMap<Int, String> = HashMap<Int, String>()
    private var portalAccount: String? = null
    private var nickName: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun showSignUpCompleteDialog(){
        val dialogListener = View.OnClickListener {
            dialog?.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        showSimpleDialog(
            title = getString(R.string.sign_up_complete_dialog_title),
            message = "${nickName}님!\n한기대의 강의평가를 보러 가볼까요?",
            positiveButtonText = getString(R.string.reset_password_finished_positive_button),
            positiveButtonOnClickListener = dialogListener,
            cancelable = false
        )
    }
    private fun init() {
        portalAccount = intent.extras!!.getString("portalAccount").plus("@koreatech.ac.kr")
        nickName = intent.extras!!.getString("nickName")
        password = intent.extras!!.getString("password")
        initAppBar()
        initCheckBox()
        initEditText()
        initEvent()
        handleObserver()

    }
    private fun initEditText(){
        signUpMajorViewModel.signUpMessage.observe(this, {
            if (it.equals(getString(R.string.en_ok))) {
                dialog?.show()
            }
        })
    }
    private fun handleObserver(){
        signUpMajorViewModel.signUpMessage.observe(this, {
            if (it.equals(getString(R.string.en_ok))) {
                showSignUpCompleteDialog()
            }
        })
    }
    private fun initEvent() {
        binding.majorCompleteButton.setOnClickListener {
            if (binding.majorCompleteButton.isEnabled) {
                major = ArrayList(majorHashMap.values).toArray(arrayOfNulls(majorHashMap.values.size))
                signUpMajorViewModel.signUp(major, nickName!!, password!!, portalAccount!!)
            }
        }
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun initCheckBox() {
        binding.mechanicalEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(1, getString(R.string.mechanical_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(1)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.designEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(2, getString(R.string.design_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(2)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.mechatronicsEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(3, getString(R.string.mechatronics_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(3)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.industryManagementCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(4, getString(R.string.industrial_management))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(4)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.energyEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(5, getString(R.string.energy_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(5)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.electronicEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(6, getString(R.string.electronic_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(6)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
        binding.computerEngineeringCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                majorHashMap.put(7, getString(R.string.computer_engineering))
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            } else {
                majorHashMap.remove(7)
                binding.majorCompleteButton.isEnabled = checkBoxCheck()
            }
        }
    }

    private fun checkBoxCheck(): Boolean {
        if (binding.mechanicalEngineeringCheckbox.isChecked || binding.designEngineeringCheckbox.isChecked || binding.mechatronicsEngineeringCheckbox.isChecked ||
            binding.industryManagementCheckbox.isChecked || binding.energyEngineeringCheckbox.isChecked || binding.electronicEngineeringCheckbox.isChecked ||
            binding.computerEngineeringCheckbox.isChecked
        ) {
            return true
        } else {
            return false
        }
    }
}