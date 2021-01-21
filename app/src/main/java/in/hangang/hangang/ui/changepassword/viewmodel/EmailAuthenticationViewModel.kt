package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EmailAuthenticationViewModel(val userRepository: UserRepository) : ViewModelBase() {
    private val _sentEmailAuth = MutableLiveData(false)
    private val _finishedEmailAuth = MutableLiveData(false)

    private val _showResentEmailAuthNumberDialog = MutableLiveData(false)
    private val _showEmailAuthFailedDialog = MutableLiveData(false)

    val sentEmailAuth : LiveData<Boolean> get() = _sentEmailAuth
    val showResentEmailAuthNumberDialog : LiveData<Boolean> get() = _showResentEmailAuthNumberDialog
    val showEmailAuthFailedDialog : LiveData<Boolean> get() = _showEmailAuthFailedDialog
    val finishedEmailAuth : LiveData<Boolean> get() = _finishedEmailAuth
    
    fun sendAuthNumber(portalAccount : String) {
        if(sentEmailAuth.value == true) {
            resendAuthNumber(portalAccount)
        } else {
            userRepository.emailPasswordCheck(portalAccount)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _sentEmailAuth.postValue(true)
                }, {
                    LogUtil.e("Error in send auth number : ${it.message}")
                    _showEmailAuthFailedDialog.postValue(true)
                })
        }
    }

    private fun resendAuthNumber(portalAccount : String) {
        _showResentEmailAuthNumberDialog.postValue(false)
        userRepository.emailPasswordCheck(portalAccount)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _showResentEmailAuthNumberDialog.postValue(true)
            }, {
                LogUtil.e("Error in resend auth number : ${it.message}")
                _showEmailAuthFailedDialog.postValue(true)
            })
    }

    fun finishEmailAuth(portalAccount : String, secret : String) {
        _showEmailAuthFailedDialog.postValue(false)
        userRepository.emailPasswordConfig(portalAccount, secret)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _finishedEmailAuth.postValue(true)
            }, {
                LogUtil.e("Error in finishing email auth : ${it.message}")
                _showEmailAuthFailedDialog.postValue(true)
            })
    }
}