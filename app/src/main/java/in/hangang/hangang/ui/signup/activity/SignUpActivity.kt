package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.CHECK
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.ERROR
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.UNDEFINED
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_LENGTH
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.MASK_ERR_NO_INPUT
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.NO_ERR
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpBinding
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpViewModel
import `in`.hangang.hangang.util.debounce
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up
    private val signUpViewModel: SignUpViewModel by viewModel() {
        parametersOf(
            intent.getStringExtra(
                "id"
            )
        )
    }
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = signUpViewModel
        init()
    }

    private fun init() {

        id = signUpViewModel.id.toString()
        //id = intent.extras!!.getString("id")
        binding.idEditText.text = id!!.toEditable()
        binding.idEditText.isEditTextEnabled = false //editText를 수정 불가로 설정
        initAppBar()
        initEvent()
        handleObserver()
        initTextWatcher()

    }

    private fun initEvent() {
        binding.signUpNextButton.setOnClickListener {
            if (binding.signUpNextButton.isEnabled) {
                val intent = Intent(this, SignUpMajorActivity::class.java)
                intent.putExtra("portalAccount", id)
                intent.putExtra("password", binding.passwordEditText.text.toString())
                intent.putExtra("nickName", binding.nicknameEditText.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun handleObserver() {
        binding.nicknameEditText.debounce()
            .subscribeOn(Schedulers.io())
            .subscribe { signUpViewModel.checkNickName(it) }

        signUpViewModel.nickNameCheckText.observe(this, {
            if (it.equals(getString(R.string.available_nickname))) {
                if (binding.nicknameEditText.text.toString().length == 0) {
                    binding.nicknameEditText.status = UNDEFINED
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                } else {
                    binding.nicknameEditText.status = CHECK
                    binding.nicknameEditErrorText.text = ""
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                }
            } else {
                if (binding.nicknameEditText.text.toString().length == 0) {
                    binding.nicknameEditText.status = UNDEFINED
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                    binding.nicknameEditErrorText.text = ""
                } else {
                    binding.nicknameEditText.status = ERROR
                    binding.nicknameEditErrorText.text = it
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                }
            }
        })
        signUpViewModel.isPasswordAvailable.observe(this, {
            if (it) {
                binding.signUpNextButton.isEnabled = true
            }
        })
    }

    private fun initTextWatcher() {
        with(binding) {
            passwordEditText.addTextChangedListener {
                passwordErrorText.text =
                    generatePasswordRegexErrorString()
                if (passwordEditText.errorCode == NO_ERR)
                    passwordEditText.status = CHECK
                else
                    passwordEditText.status = ERROR

                if (passwordCheckEditText.text.toString().equals(it.toString()))
                    passwordCheckEditText.status = CHECK
                else
                    passwordCheckEditText.status = ERROR

                signUpViewModel.isPasswordAvailable.value = isConditionComplete()
            }
            passwordCheckEditText.addTextChangedListener {
                var currentString = passwordCheckEditText.text.toString()
                if (passwordEditText.text.toString().equals(currentString))
                    passwordCheckEditText.status = CHECK
                else
                    passwordCheckEditText.status = ERROR

                signUpViewModel.isPasswordAvailable.value = isConditionComplete()
            }
        }
    }

    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private fun isConditionComplete(): Boolean {
        with(binding) {
            return passwordEditText.status == CHECK && passwordCheckEditText.status == CHECK && nicknameEditText.status == CHECK
        }

    }

    private fun generatePasswordRegexErrorString(): String =
        with(binding.passwordEditText) {
            when {
                errorCode == NO_ERR ->
                    ""
                isErrorIncluded(MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS) ->
                    getString(R.string.reset_password_regex_error_included_not_supported_characters)
                isErrorIncluded(MASK_ERR_NO_INPUT) ->
                    getString(R.string.reset_password_regex_error_no_inputs)
                isErrorIncluded(MASK_ERR_LENGTH) ->
                    generatePasswordRegexLengthErrorString(errorCode)
                else ->
                    generatePasswordRegexNotIncludedSomeCharactersError(errorCode)
            }
        }

    private fun generatePasswordRegexLengthErrorString(errorCode: Int) =
        when (errorCode and MASK_ERR_LENGTH) {
            PasswordEditTextWithRegex.ERR_LENGTH_TOO_SHORT -> getString(R.string.reset_password_regex_error_too_short)
            PasswordEditTextWithRegex.ERR_LENGTH_TOO_LONG -> getString(R.string.reset_password_regex_error_too_long)
            else -> ""
        }

    private fun generatePasswordRegexNotIncludedSomeCharactersError(errorCode: Int): String {
        val stringBuilder = StringBuilder()

        if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_ENGLISH == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_ENGLISH)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_english))
        if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_NUMBER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_NUMBER)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_number))
        if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_SPECIAL_CHARACTER)
            stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_specials))

        return getString(R.string.reset_password_regex_error_not_included, stringBuilder.toString())
    }

}