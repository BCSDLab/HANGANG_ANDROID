package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.MutableLiveData

class ChangePasswordFragmentViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    val passwordRegexErrorMessage = MutableLiveData("")

    fun applyNewPassword(portalAccount: String,
                         password: String,
                         onSuccess: ((CommonResponse) -> Unit) = { _ -> },
                         onError: ((Throwable) -> Unit) = { _ -> }) {
        userRepository.changePassword(portalAccount, password)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    onSuccess(it)
                }, {
                    LogUtil.e("Error in changing password : ${it.toCommonResponse().errorMessage}")
                    onError(it)
                })
    }

}