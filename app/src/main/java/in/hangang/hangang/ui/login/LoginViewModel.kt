package `in`.hangang.hangang.ui.login

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.util.toSHA256
import `in`.hangang.hangang.HangangApplication
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class LoginViewModel(private val userRepository: UserRepository) : ViewModelBase() {

    private val _isLoginSuccess = MutableLiveData(false)
    val isLoginSuccess: LiveData<Boolean>
        get() = _isLoginSuccess

    private val _errorConfig = MutableLiveData<Boolean>()
    val errorConfig: LiveData<Boolean>
        get() = _errorConfig

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    fun loginButtonClick(portalID: String, password: String){
        userRepository.login(portalID, password)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({ data ->
                LogUtil.d(data.httpStatus)
                _isLoginSuccess.value = true
            }, { _ ->
                _errorConfig.value = true
            }).addTo(compositeDisposable)


    }


}