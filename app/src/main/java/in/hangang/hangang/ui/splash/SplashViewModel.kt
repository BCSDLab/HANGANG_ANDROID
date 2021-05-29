package `in`.hangang.hangang.ui.splash

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class SplashViewModel(private val userRepository:UserRepository):ViewModelBase() {

    private val _isTokenValid = MutableLiveData<Boolean>()
    val isTokenValid : LiveData<Boolean>
    get() = _isTokenValid



    fun checkAccessTokenValid(){
        userRepository.getAutoLoginStatus()
            .flatMap { isAutoLoginValid ->
                when(isAutoLoginValid){
                    true -> userRepository.checkAccessTokenValid()
                    false -> Single.error(Throwable("Go to Login Page"))
            } }.handleHttpException()
            .handleProgress(this)
            .withThread()
            .flatMap { userRepository.updateToken() }
            .subscribe({
                _isTokenValid.value = true
            },{
                _isTokenValid.value = false
        }).addTo(compositeDisposable)
    }
}
