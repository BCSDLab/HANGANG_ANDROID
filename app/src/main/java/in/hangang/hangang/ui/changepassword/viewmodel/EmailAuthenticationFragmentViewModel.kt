package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EmailAuthenticationFragmentViewModel(val userRepository: UserRepository) : ViewModelBase() {
    val portalAccount = MutableLiveData("")

    private val _sentEmailAuth = MutableLiveData(false)
    val sentEmailAuth: LiveData<Boolean> get() = _sentEmailAuth

    fun sendAuthNumber(portalAccount: String,
                       onSuccess: ((CommonResponse) -> Unit) = { _ -> },
                       onError: ((Throwable) -> Unit) = { _ -> }) {
        if (sentEmailAuth.value == true) {
            resendAuthNumber(portalAccount, onSuccess, onError)
        } else {
            userRepository.emailPasswordCheck(portalAccount)
                    .handleHttpException()
                    .handleProgress(this)
                    .withThread()
                    .subscribe({
                        onSuccess(it)
                        _sentEmailAuth.postValue(true)
                    }, {
                        LogUtil.e("Error in send auth number : ${it.toCommonResponse().errorMessage}")
                        onError(it)
                    })
        }
    }

    private fun resendAuthNumber(portalAccount: String,
                                 onSuccess: ((CommonResponse) -> Unit) = { _ -> },
                                 onError: ((Throwable) -> Unit) = { _ -> }) {
        userRepository.emailPasswordCheck(portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    onSuccess(it)
                }, {
                    LogUtil.e("Error in resend auth number : ${it.toCommonResponse().errorMessage}")
                    onError(it)
                })
    }

    fun finishEmailAuth(portalAccount: String, secret: String,
                        onSuccess: ((CommonResponse) -> Unit) = { _ -> },
                        onError: ((Throwable) -> Unit) = { _ -> }) {
        userRepository.emailPasswordConfig(portalAccount, secret)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    onSuccess(it)
                }, {
                    LogUtil.e("Error in finishing email auth : ${it.toCommonResponse().errorMessage}")
                    onError(it)
                })
    }
}