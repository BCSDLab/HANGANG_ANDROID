package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.util.toSHA256
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChangePasswordFragmentViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    val passwordRegexErrorMessage = MutableLiveData("")
    private val _changePasswordResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<Throwable>()

    val changePasswordResponse: LiveData<CommonResponse> get() = _changePasswordResponse
    val throwable: LiveData<Throwable> get() = _throwable

    fun applyNewPassword(
            portalAccount: String,
            password: String
    ) {
        userRepository.changePassword(portalAccount, password.toSHA256())
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _changePasswordResponse.value = it
                }, {
                    LogUtil.e("Error in changing password : ${it.toCommonResponse().errorMessage}")
                    _throwable.value = it
                })
    }

}