package `in`.hangang.hangang.ui.signup.viewmodel

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
import io.reactivex.rxjava3.kotlin.addTo
import retrofit2.HttpException

class SignUpEmailViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _emailSendText = MutableLiveData<String>()
    val emailSendText: LiveData<String>
        get() = _emailSendText
    private val _emainConfigSendText = MutableLiveData<String>()
    val emailConfigSendText: LiveData<String>
        get() = _emainConfigSendText
    val sentEmailAuth = MutableLiveData<AuthEmailState>(AuthEmailState.UNACTIVE)
    fun sendEmail(portalID: String) {
        userRepository.emailCheck(portalID)
            .handleProgress(this)
            .withThread()
            .onErrorReturn {
                return@onErrorReturn it.toCommonResponse()
            }
            .subscribe({ data ->
                if (data.code != 200) _emailSendText.value = data.errorMessage

            },
                { error -> LogUtil.e("error") })
            .addTo(compositeDisposable)
    }

    fun sendEmailConfig(portalID: String, secret: String) {
        userRepository.emailConfig(portalID, secret)
            .handleProgress(this)
            .withThread()
            .onErrorReturn {
                return@onErrorReturn it.toCommonResponse()
            }
            .subscribe({ data ->
                if (data.code == 200) {
                    _emainConfigSendText.value = "OK"
                } else {
                    _emainConfigSendText.value = data.errorMessage
                }
            },
                { error ->
                    LogUtil.e(error.toCommonResponse().errorMessage)
                })
            .addTo(compositeDisposable)
    }

    enum class AuthEmailState {
        UNACTIVE, ACTIVE, RETRY
    }
}