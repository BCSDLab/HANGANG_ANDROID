package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class DashBoardViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    val nickNameCheckText = MutableLiveData<String>()
    val emailSendText = MutableLiveData<String>()

    fun checkNickName(nickName: String) {
        userRepository.checkNickname(nickName)
            .toSingleConvert()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { nickNameCheckText.postValue("검색중") }
            .subscribe({ data -> nickNameCheckText.value = data.toString() },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }


    fun sendEmail(portalID : String){
        userRepository.emailCheck(portalID)
            .toSingleConvert()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .doOnSubscribe { emailSendText.postValue("전송중") }
            .subscribe({ data -> emailSendText.value = data.toString() },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }
}