package `in`.hangang.hangang.ui.signup.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.CHECK
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.ERROR
import `in`.hangang.core.view.edittext.EditTextWithError.Companion.UNDEFINED
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex
import `in`.hangang.core.view.edittext.PasswordEditTextWithRegex.Companion.NO_ERR
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySignUpBinding
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpViewModel
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up
    private val signUpViewModel: SignUpViewModel by viewModel()
    lateinit var newPasswordTextWatcher: TextWatcher
    lateinit var passwordCheckWatcher: TextWatcher
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = signUpViewModel
        init()
    }

    private fun init() {
        id = intent.extras!!.getString("id")
        binding.idEditText.text = id!!.toEditable()
        binding.idEditText.isEditTextEnabled = false //editText를 수정 불가로 설정
        initAppBar()
        initEvent()
        handleObserver()
        initTextWatcher()
        binding.passwordEditText.addTextChangedListener(newPasswordTextWatcher)
        binding.passwordCheckEditText.addTextChangedListener(passwordCheckWatcher)

    }
    private fun initEvent(){
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
        Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<String>? ->
            binding.nicknameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    emitter?.onNext(s.toString())
                }
            })

        })
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable?) {
                }

                override fun onNext(t: String) {
                    LogUtil.e(t.length.toString())
                    signUpViewModel.checkNickName(t)
                }

                override fun onError(e: Throwable?) {
                }

                override fun onComplete() {
                }
            })
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
        newPasswordTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.passwordErrorText.text =
                    generatePasswordRegexErrorString(binding.passwordEditText.errorCode)
                if (binding.passwordEditText.errorCode == NO_ERR) {
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                } else
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
            }
        }

        passwordCheckWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var currentString = binding.passwordCheckEditText.text.toString()
                if (binding.passwordEditText.text.toString().equals(currentString)) {
                    binding.passwordCheckEditText.status = CHECK
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                } else {
                    binding.passwordCheckEditText.status = ERROR
                    signUpViewModel.isPasswordAvailable.value = isConditionComplete()
                }
            }
        }
    }


    private fun initAppBar() {
        binding.appBar.title = getString(R.string.join_in)
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private fun isConditionComplete(): Boolean {
        if (binding.passwordEditText.status == CHECK && binding.passwordCheckEditText.status == CHECK && binding.nicknameEditText.status == CHECK)
            return true
        else
            return false
    }

    private fun generatePasswordRegexErrorString(errorCode: Int): String {
        return if (errorCode == PasswordEditTextWithRegex.NO_ERR) {
            ""
        } else if (errorCode and PasswordEditTextWithRegex.MASK_ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS == PasswordEditTextWithRegex.ERR_CONTAINS_NOT_SUPPORTED_CHARACTERS)
            getString(R.string.reset_password_regex_error_included_not_supported_characters)
        else if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NO_INPUT == PasswordEditTextWithRegex.ERR_NO_INPUT)
            getString(R.string.reset_password_regex_error_no_inputs)
        else if (errorCode and PasswordEditTextWithRegex.MASK_ERR_LENGTH != PasswordEditTextWithRegex.NO_ERR) {
            when (errorCode and PasswordEditTextWithRegex.MASK_ERR_LENGTH) {
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_SHORT -> getString(R.string.reset_password_regex_error_too_short)
                PasswordEditTextWithRegex.ERR_LENGTH_TOO_LONG -> getString(R.string.reset_password_regex_error_too_long)
                else -> "."
            }
        } else {
            val stringBuilder = StringBuilder()

            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_ENGLISH == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_ENGLISH)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_english))
            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_NUMBER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_NUMBER)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_number))
            if (errorCode and PasswordEditTextWithRegex.MASK_ERR_NOT_CONTAINS_SPECIAL_CHARACTER == PasswordEditTextWithRegex.ERR_NOT_CONTAINS_SPECIAL_CHARACTER)
                stringBuilder.append(getString(R.string.reset_password_regex_error_not_included_reason_specials))

            getString(R.string.reset_password_regex_error_not_included, stringBuilder.toString())
        }
    }
}