package `in`.hangang.hangang.ui.signup.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.util.toSHA256
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class SignUpMajorViewModel(private val userRepository: UserRepository, private val _portalAccount: String, private val _nickName: String, private val _password: String) : ViewModelBase() {
    private val _signUpMessage = MutableLiveData<String>()
    val signUpMessage: LiveData<String>
        get() = _signUpMessage
    val portalAccount: String = _portalAccount
    val nickName: String = _nickName
    val password: String = _password
    var majorHashMap: HashMap<Int, String> = HashMap<Int, String>()
    var major = emptyArray<String>()
    fun signUp( major: Array<String>,
                nickName: String,
                password: String,
                portalAccount: String){
        val hashPassword = password.toSHA256()
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