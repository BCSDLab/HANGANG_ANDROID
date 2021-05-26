package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class SignUpEmailViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _emailSendText = MutableLiveData<String>()
    val emailSendText: LiveData<String>
        get() = _emailSendText
    private val _emainConfigSendText = MutableLiveData<Boolean>()
    val emailConfigSendText: LiveData<Boolean>
        get() = _emainConfigSendText
    private val _errorConfig = MutableLiveData<Boolean>()
    val errorConfig: LiveData<Boolean>
        get() = _errorConfig
    var isAuthNumSend = false // 한번이라도 인증번호 전송을 눌렀는지 확인
    var isAuthNumable = false // 인증번호 전송활성화 여부
    fun sendEmail(portalID: String) {
        userRepository.emailCheck(portalID)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({ data -> _emailSendText.value = data.message },
                { error -> LogUtil.e(error.toCommonResponse().message) })
            .addTo(compositeDisposable)
    }

    fun sendEmailConfig(portalID: String, secret: String) {
        userRepository.emailConfig(portalID, secret)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({ data -> _emainConfigSendText.value = data.httpStatus.equals("OK") },
                { error ->
                    _errorConfig.value = true
                })
            .addTo(compositeDisposable)
    }
}