package `in`.hangang.hangang.ui.resetpassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NewPasswordViewModel {

    private val _passwordRegexErrorMessage = MutableLiveData("")
    val passwordRegexErrorMessage: LiveData<String>
        get() = _passwordRegexErrorMessage

    fun applyNewPassword() {
        TODO("isLoading on")
        TODO("new password api")
    }

}