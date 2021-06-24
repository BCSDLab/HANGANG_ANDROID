package `in`.hangang.hangang.ui.settings.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class MyProfileActivityViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _throwable = MutableLiveData<Throwable>()
    private val _myProfile = MutableLiveData<MyProfileResponse>()
    private val _isProfileEdited = MutableLiveData<Boolean>()
    private val _nickNameEditStatus = MutableLiveData<Boolean>()


    val throwable: LiveData<Throwable>
        get() = _throwable
    val myProfile: LiveData<MyProfileResponse>
        get() = _myProfile
    val appBarRightButton: LiveData<Boolean>
        get() = _isProfileEdited
    val nickNameEditStatus: LiveData<Boolean>
        get() = _nickNameEditStatus

    fun init() {
        if (_myProfile.value == null) {
            getMyProfile()
        }
    }

    fun getMyProfile() {
        userRepository.getMyProfile()
            .withThread()
            .handleProgress(this)
            .handleHttpException()
            .subscribe({
                _myProfile.value = (it)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }


    fun applyMyProfile(name: String, nickName: String, major: Array<String>) {
        userRepository.checkNickname(nickName)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _nickNameEditStatus.value = true
                userRepository.saveProfile(name, nickName, major)
                    .handleHttpException()
                    .handleProgress(this)
                    .withThread()
                    .subscribe({
                        _isProfileEdited.value = true
                    }, {
                        _isProfileEdited.value = false
                        LogUtil.e("Error in editing my profile : ${it.toCommonResponse().errorMessage}")
                        _throwable.value = it
                    })
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)

            })

    }
}

