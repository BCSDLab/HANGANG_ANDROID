package `in`.hangang.hangang.ui.settings.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class MyProfileActivityViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private var _changeNicknameResponse = MutableLiveData<CommonResponse>()
    private val _throwable = MutableLiveData<Throwable>()
    private val _deleteAccountResponse = MutableLiveData<CommonResponse>()
    private val _myProfile = MutableLiveData<MyProfileResponse>()
    private val _isProfileEdited = MutableLiveData<Boolean>()
    private val _nickNameEditStatus = MutableLiveData<CommonResponse>()


    val changeNicknameResponse: LiveData<CommonResponse>
        get() = _changeNicknameResponse
    val throwable: LiveData<Throwable>
        get() = _throwable
    val deleteAccountResponse: MutableLiveData<CommonResponse>
        get() = _deleteAccountResponse
    val myProfile: LiveData<MyProfileResponse>
        get() = _myProfile
    val appBarRightButton: LiveData<Boolean>
        get() = _isProfileEdited

    val nickNameEditStutus: LiveData<CommonResponse>
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
            userRepository.saveProfile(name, nickName, major)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _isProfileEdited.value = true
                }, {
                    _isProfileEdited.value = false
                    _nickNameEditStatus.value = CommonResponse()
                    LogUtil.e("Error in editing my profile : ${it.toCommonResponse().errorMessage}")
                    _throwable.value = it
                })
        }
    }

