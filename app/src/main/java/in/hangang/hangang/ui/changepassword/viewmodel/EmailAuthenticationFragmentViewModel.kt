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

class EmailAuthenticationFragmentViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _portalAccount = MutableLiveData("")
    private val _sentEmailAuth = MutableLiveData(false)
    private val _sendAuthNumberResponse = MutableLiveData<CommonResponse>()
    private val _resendAuthNumberResponse = MutableLiveData<CommonResponse>()
    private val _finishEmailAuthResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<CommonResponse>()
    private val _emailErrorMessage = MutableLiveData<String>()
    private val _emailAuthNumberErrorMessage = MutableLiveData<String>()

    val portalAccount : LiveData<String> get() = _portalAccount
    val sentEmailAuth: LiveData<Boolean> get() = _sentEmailAuth
    val throwable : LiveData<CommonResponse> get() = _throwable

    val sendAuthNumberResponse : LiveData<CommonResponse> get() = _sendAuthNumberResponse
    val resendAuthNumberResponse : LiveData<CommonResponse> get() = _resendAuthNumberResponse
    val finishEmailAuthResponse : LiveData<CommonResponse> get() = _finishEmailAuthResponse

    val emailErrorMessage : LiveData<String> get() = _emailErrorMessage
    val emailAuthNumberErrorMessage : LiveData<String> get() = _emailAuthNumberErrorMessage

    fun sendAuthNumber(portalAccount: String) {
        if (sentEmailAuth.value == true) {
            resendAuthNumber(portalAccount)
        } else {
            userRepository.emailPasswordCheck(portalAccount)
                    .handleHttpException()
                    .handleProgress(this)
                    .withThread()
                    .subscribe({
                        _sendAuthNumberResponse.value = it
                        _sentEmailAuth.postValue(true)
                        _emailErrorMessage.postValue("")
                        _emailAuthNumberErrorMessage.postValue("")
                    }, {
                        with(it.toCommonResponse()) {
                            _emailErrorMessage.postValue(this.errorMessage)
                            LogUtil.e("Error in send auth number : ${this.errorMessage}")
                            _throwable.value = this
                        }
                    })
        }
    }

    private fun resendAuthNumber(portalAccount: String) {
        userRepository.emailPasswordCheck(portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _resendAuthNumberResponse.value = it
                    _emailErrorMessage.postValue("")
                    _emailAuthNumberErrorMessage.postValue("")
                }, {
                    with(it.toCommonResponse()) {
                        LogUtil.e("Error in resend auth number : ${this.errorMessage}")
                        _throwable.value = this
                    }
                })
    }

    fun finishEmailAuth(portalAccount: String, secret: String) {
        userRepository.emailPasswordConfig(portalAccount, secret)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _finishEmailAuthResponse.value = it
                }, {
                    with(it.toCommonResponse()) {
                        _emailAuthNumberErrorMessage.postValue(this.errorMessage)
                        LogUtil.e("Error in finishing email auth : ${this.errorMessage}")
                        _throwable.value = this
                    }

                })
    }
}