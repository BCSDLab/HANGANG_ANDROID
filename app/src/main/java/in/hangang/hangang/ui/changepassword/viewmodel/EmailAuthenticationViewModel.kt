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

class EmailAuthenticationViewModel(val userRepository: UserRepository) : ViewModelBase() {
    private val _sentEmailAuth = MutableLiveData(false)

    val sentEmailAuth: LiveData<Boolean> get() = _sentEmailAuth

    fun sendAuthNumber(portalAccount: String,
                       onSuccess: ((CommonResponse) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        if (sentEmailAuth.value == true) {
            resendAuthNumber(portalAccount, onSuccess, onError)
        } else {
            userRepository.emailPasswordCheck(portalAccount)
                    .handleHttpException()
                    .handleProgress(this)
                    .withThread()
                    .subscribe({
                        onSuccess?.let { it1 -> it1(it) }
                        _sentEmailAuth.postValue(true)
                    }, {
                        onError?.let { it1 -> it1(it) }
                        LogUtil.e("Error in send auth number : ${it.toCommonResponse().errorMessage}")
                    })
        }
    }

    private fun resendAuthNumber(portalAccount: String,
                                 onSuccess: ((CommonResponse) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        userRepository.emailPasswordCheck(portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    onSuccess?.let { it1 -> it1(it) }
                }, {
                    onError?.let { it1 -> it1(it) }
                    LogUtil.e("Error in resend auth number : ${it.toCommonResponse().errorMessage}")
                })
    }

    fun finishEmailAuth(portalAccount: String, secret: String,
                        onSuccess: ((CommonResponse) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        userRepository.emailPasswordConfig(portalAccount, secret)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    onSuccess?.let { it1 -> it1(it) }
                }, {
                    onError?.let { it1 -> it1(it) }
                    LogUtil.e("Error in finishing email auth : ${it.toCommonResponse().errorMessage}")
                })
    }
}