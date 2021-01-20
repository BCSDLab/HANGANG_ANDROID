package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo
import retrofit2.HttpException

class DashBoardViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _nickNameCheckText = MutableLiveData<String>()
    val nickNameCheckText: LiveData<String>
        get() = _nickNameCheckText
    private val _emailSendText = MutableLiveData<String>()
    val emailSendText: LiveData<String>
        get() = _emailSendText

    fun checkNickName(nickName: String) {
        userRepository.checkNickname(nickName)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { _nickNameCheckText.postValue("검색중") }
            .subscribe({ data -> _nickNameCheckText.value = data.message },
                { error -> LogUtil.e(error.message)})
            .addTo(compositeDisposable)
    }


    fun sendEmail(portalID: String) {
        userRepository.emailCheck(portalID)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { _emailSendText.postValue("전송중") }
            .subscribe({ data -> _emailSendText.value = data.message },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }
}