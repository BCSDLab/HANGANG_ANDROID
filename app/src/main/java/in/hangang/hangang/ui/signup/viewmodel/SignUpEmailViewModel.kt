package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
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
    private val _emainConfigSendText = MutableLiveData<String>()
    val emailConfigSendText: LiveData<String>
        get() = _emainConfigSendText

    fun sendEmail(portalID: String) {
        userRepository.emailCheck(portalID)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { _emailSendText.postValue("전송중") }
            .subscribe({ data -> _emailSendText.value = data.message },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }

    fun sendEmailConfig(portalID: String, secret: String) {
        userRepository.emailConfig(portalID, secret)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { _emainConfigSendText.postValue("확인중") }
            .subscribe({ data -> _emainConfigSendText.value = data.httpStatus },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }
}