package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.util.HashGeneratorUtil
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class SignUpMajorViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _signUpMessage = MutableLiveData<String>()
    val signUpMessage: LiveData<String>
        get() = _signUpMessage

    fun signUp( major: Array<String>,
                nickName: String,
                password: String,
                portalAccount: String){
        val hashPassword = HashGeneratorUtil.generateSHA256(password)
        LogUtil.e(hashPassword)
        hashPassword?.let {
            userRepository.signUp(major,nickName, it,portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .doOnSubscribe {  }
                .subscribe({ data -> _signUpMessage.value = data.httpStatus },
                    { error ->
                        error.toCommonResponse().httpStatus?.let {
                            _signUpMessage.value = it
                        }
                    }).addTo(compositeDisposable)
        }

    }
}