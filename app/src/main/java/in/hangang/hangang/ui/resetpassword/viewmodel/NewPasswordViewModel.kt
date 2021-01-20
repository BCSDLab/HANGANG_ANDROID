package `in`.hangang.hangang.ui.resetpassword.viewmodel

import androidx.lifecycle.MutableLiveData

class NewPasswordViewModel {
    val newPassword = MutableLiveData("")
    val newPasswordConfirm = MutableLiveData("")

    private val _passwordRegexErrorMessage = MutableLiveData("")
    val passwordRegexErrorMessage get() = _passwordRegexErrorMessage

    fun applyNewPassword() {
        TODO("isLoading on")
        TODO("new password api")
    }

}