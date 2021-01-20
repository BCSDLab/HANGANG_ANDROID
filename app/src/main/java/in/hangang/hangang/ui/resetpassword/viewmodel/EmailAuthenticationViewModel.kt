package `in`.hangang.hangang.ui.resetpassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EmailAuthenticationViewModel : ViewModelBase() {
    private val _sentEmailAuth = MutableLiveData(false)
    private val _finishedEmailAuth = MutableLiveData(false)

    private val _showResentEmailAuthNumberDialog = MutableLiveData(false)
    private val _showEmailAuthFailedDialog = MutableLiveData(false)

    val sentEmailAuth : LiveData<Boolean> get() = _sentEmailAuth
    val showResentEmailAuthNumberDialog : LiveData<Boolean> get() = _showResentEmailAuthNumberDialog
    val showEmailAuthFailedDialog : LiveData<Boolean> get() = _showEmailAuthFailedDialog
    val finishedEmailAuth : LiveData<Boolean> get() = _finishedEmailAuth
    
    fun sendAuthNumber() {
        if(sentEmailAuth.value == true) {
            resendAuthNumber()
        } else {
            TODO("Make isLoading true")
            TODO("API get")
            TODO("Make isLoading false")

            TODO("If right email")
            _sentEmailAuth.postValue(true)
            TODO("Else wrong email")
        }
    }

    private fun resendAuthNumber() {
        showResentEmailAuthNumberDialog.postValue(false)
        TODO("Make isLoading true")
        TODO("API")
        TODO("Make isLoading false")

        TODO("If right email")
        showResentEmailAuthNumberDialog.postValue(true)
        TODO("Else wrong email")
    }

    fun finishEmailAuth() {
        _showEmailAuthFailedDialog.postValue(false)
        TODO("Make isLoading true")
        TODO("API")
        TODO("Make isLoading false")
        TODO("If email auth success")
        _finishedEmailAuth.postValue(true)
        TODO("else failed")
        _showEmailAuthFailedDialog.postValue(true)
    }
}