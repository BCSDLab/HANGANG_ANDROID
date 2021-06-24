package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EmailAuthenticationFragmentViewModel(private val userRepository: UserRepository) :
        ViewModelBase() {
    private val _portalAccount = MutableLiveData("")
    private val _sentEmailAuth = MutableLiveData(false)
    private val _sendAuthNumberResponse = MutableLiveData<CommonResponse>()
    private val _resendAuthNumberResponse = MutableLiveData<CommonResponse>()
    private val _finishEmailAuthResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<CommonResponse>()
    private val _emailErrorMessage = MutableLiveData<String?>()
    private val _emailAuthNumberErrorMessage = MutableLiveData<String?>()

    val portalAccount: LiveData<String> get() = _portalAccount
    val sentEmailAuth: LiveData<Boolean> get() = _sentEmailAuth
    val throwable: LiveData<CommonResponse> get() = _throwable

    val sendAuthNumberResponse: LiveData<CommonResponse> get() = _sendAuthNumberResponse
    val resendAuthNumberResponse: LiveData<CommonResponse> get() = _resendAuthNumberResponse
    val finishEmailAuthResponse: LiveData<CommonResponse> get() = _finishEmailAuthResponse

    val emailErrorMessage: LiveData<String?> get() = _emailErrorMessage
    val emailAuthNumberErrorMessage: LiveData<String?> get() = _emailAuthNumberErrorMessage

    fun sendAuthNumber(portalAccount: String) {
        if (sentEmailAuth.value == true) {
            resendAuthNumber(portalAccount)
        } else {
            userRepository.emailPasswordCheck(portalAccount)
                    .handleHttpException()
                    .handleProgress(this)
                    .withThread()
                    .subscribeWithCommonResponseError({
                        _sendAuthNumberResponse.value = it
                        _sentEmailAuth.postValue(true)
                        _emailErrorMessage.postValue("")
                        _emailAuthNumberErrorMessage.postValue("")
                    }, {
                        _emailErrorMessage.postValue(it.errorMessage)
                        LogUtil.e("Error in send auth number : ${it.errorMessage}")
                        _throwable.value = it
                    })
        }
    }

    private fun resendAuthNumber(portalAccount: String) {
        userRepository.emailPasswordCheck(portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribeWithCommonResponseError({
                    _resendAuthNumberResponse.value = it
                    _emailErrorMessage.postValue("")
                    _emailAuthNumberErrorMessage.postValue("")
                }, {
                    LogUtil.e("Error in resend auth number : ${it.errorMessage}")
                    _throwable.value = it
                })
    }

    fun finishEmailAuth(portalAccount: String, secret: String) {
        userRepository.emailPasswordConfig(portalAccount, secret)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribeWithCommonResponseError({
                    _finishEmailAuthResponse.value = it
                }, {
                    _emailAuthNumberErrorMessage.postValue(it.errorMessage)
                    LogUtil.e("Error in finishing email auth : ${it.errorMessage}")
                    _throwable.value = it
                })
    }
}