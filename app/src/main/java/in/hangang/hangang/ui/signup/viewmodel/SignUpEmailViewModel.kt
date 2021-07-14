package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo
import retrofit2.HttpException

class SignUpEmailViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _emailSendText = MutableLiveData<Int>()
    val emailSendText: LiveData<Int>
        get() = _emailSendText

    val showAlert = MutableLiveData<Boolean>()

    private val _emainConfigSendText = MutableLiveData<CommonResponse>()
    val emailConfigSendText: LiveData<CommonResponse>
        get() = _emainConfigSendText
    val sentEmailAuth = MutableLiveData<AuthEmailState>(AuthEmailState.UNACTIVE)
    fun sendEmail(portalID: String) {
        userRepository.emailCheck(portalID)
            .handleProgress(this)
            .withThread()
            .subscribe({ data ->
                if (data.flag == 0) showAlert.value = true
            },
                {
                    val code = it.toCommonResponse().code ?: -1
                    _emailSendText.value = code
                })
            .addTo(compositeDisposable)
    }

    fun sendEmailConfig(portalID: String, secret: String) {
        userRepository.emailConfig(portalID, secret)
            .handleProgress(this)
            .withThread()
            .subscribe({ data ->
                if(data.httpStatus == "OK")
                    _emainConfigSendText.value = data
            },
                { error ->
                    _emainConfigSendText.value = error.toCommonResponse()
                })
            .addTo(compositeDisposable)
    }

    enum class AuthEmailState {
        UNACTIVE, ACTIVE, RETRY
    }
}