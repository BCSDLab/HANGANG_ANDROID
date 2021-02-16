package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.util.toSHA256
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChangePasswordFragmentViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    val passwordRegexErrorMessage = MutableLiveData("")
    val passwordConfirmErrorMessage = MutableLiveData("")
    private val _changePasswordResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<CommonResponse>()

    val changePasswordResponse: LiveData<CommonResponse> get() = _changePasswordResponse
    val throwable: LiveData<CommonResponse> get() = _throwable

    fun applyNewPassword(portalAccount: String,
                         password: String) {
        userRepository.changePassword(portalAccount, password.toSHA256())
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribeWithCommonResponseError({
                    _changePasswordResponse.value = it
                }, {
                    LogUtil.e("Error in changing password : ${it.errorMessage}")
                    _throwable.value = it
                })
    }

}