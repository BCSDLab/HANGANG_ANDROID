package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class SignUpViewModel(private val userRepository: UserRepository, private val handle: String) : ViewModelBase() {
    private val _nickNameCheckText = MutableLiveData<String>()
    val nickNameCheckText: LiveData<String>
        get() = _nickNameCheckText
    val isPasswordAvailable = MutableLiveData<Boolean>()
    val id = handle
    fun checkNickName(nickName: String) {
        userRepository.checkNickname(nickName)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({ data -> _nickNameCheckText.value = data.message },
                        { error ->
                            error.toCommonResponse().message?.let {
                                _nickNameCheckText.value = it
                            }
                        }).addTo(compositeDisposable)
    }

}