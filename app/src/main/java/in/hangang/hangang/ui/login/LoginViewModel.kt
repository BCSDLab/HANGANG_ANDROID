package `in`.hangang.hangang.ui.login

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

class LoginViewModel(private val userRepository: UserRepository) : ViewModelBase() {

    private val _loginButtonClickResponse = MutableLiveData<CommonResponse>()
    private val _findPasswordClickResponse = MutableLiveData<CommonResponse>()
    private val _signInClickResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<Throwable>()

    val loginButtonClickResponse : LiveData<CommonResponse>get()  = _loginButtonClickResponse
    val findPasswordClickResponse : LiveData<CommonResponse>get() = _findPasswordClickResponse
    val signInClickResponse : LiveData<CommonResponse>get() = _signInClickResponse

    val throwable : LiveData<Throwable> get() = _throwable

    fun loginButtonClick(portalID: String, password: String){
        userRepository.login(portalID, password)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe( {
                _loginButtonClickResponse.value = it
            }, {
                LogUtil.e("Error in finishing email auth : ${it.toCommonResponse().errorMessage}")
                _throwable.value = it
            })

    }

}