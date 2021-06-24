package `in`.hangang.hangang.ui.settings.viewmodel

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

class SettingsActivityViewModel(private val userRepository: UserRepository) : ViewModelBase() {

    private val _autoLoginButtonResponse = MutableLiveData<Boolean>()
    val autoLoginButtonResponse : LiveData<Boolean>
    get() = _autoLoginButtonResponse

    private val _deleteAccountResponse = MutableLiveData<CommonResponse>()
    val deleteAccountResponse : LiveData<CommonResponse>
    get() = _deleteAccountResponse

    private val _logoutAll = MutableLiveData<CommonResponse>()
    val logoutAll : LiveData<CommonResponse>
        get() = _logoutAll

    private val _throwable = MutableLiveData<Throwable>()

    fun saveAutoLoginStatus(isAutoLoginActive : Boolean){
        userRepository.saveAutoLogin(isAutoLoginActive)
            .subscribe {
                _autoLoginButtonResponse.value = isAutoLoginActive
            }
    }

    fun deleteAccount(){
        userRepository.deleteAccount()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _deleteAccountResponse.value = it
            }, {
                LogUtil.e("Error in delete account : ${it.toCommonResponse().errorMessage}")
                _throwable.value = it
            })
    }

    fun logoutAll(){
        userRepository.logoutAll()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _logoutAll.value = it
            }, {
                LogUtil.e("Error in logout : ${it.toCommonResponse().errorMessage}")
                _throwable.value = it
            })
    }

}

