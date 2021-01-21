package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.MutableLiveData

class ChangePasswordViewModel(val userRepository: UserRepository) : ViewModelBase() {
    val passwordRegexErrorMessage = MutableLiveData("")

    private val _finishedChangePassword = MutableLiveData(false)

    fun applyNewPassword(portalAccount: String, password: String) {
        userRepository.changePassword(portalAccount, password)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _finishedChangePassword.postValue(true)
            }, {
                LogUtil.e("Error in changing password : ${it.message}")
            })
    }

}