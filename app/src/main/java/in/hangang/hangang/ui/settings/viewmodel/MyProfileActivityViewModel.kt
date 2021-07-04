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
    private val _isProfileEdited = MutableLiveData(false)
    private val _nickNameEditStatus = MutableLiveData(false)
    private val _nickNameCheckText = MutableLiveData<String>()
    var bottomSheetSelectedMajorList = ArrayList<String>(1)
    var nickName : String = "name"

    val throwable: LiveData<Throwable>
        get() = _throwable
    val myProfile: LiveData<MyProfileResponse>
        get() = _myProfile
    val isProfileEdited: LiveData<Boolean>
        get() = _isProfileEdited
    val nickNameEditStatus: LiveData<Boolean>
        get() = _nickNameEditStatus
    val nickNameCheckText: LiveData<String>
        get() = _nickNameCheckText

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


    fun applyMyProfileWithNickname(name: String, nickName: String, major: ArrayList<String>) {
        userRepository.checkNickname(nickName)
            .flatMap { userRepository.saveProfile(name, nickName, major) }
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _nickNameEditStatus.value = false
                setEditMode(false)
            }, {
                it.toCommonResponse().code?.let {
                    if (it == DUPLICATE_NICKNAME_CODE) {
                        _nickNameEditStatus.value = true
                        setEditMode(true)
                    }
                }
            })

    }

    fun applyMyProfile(name: String, nickName: String, major: ArrayList<String>){
        userRepository.saveProfile(name, nickName, major)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                setEditMode(false)
            },{
                it.toCommonResponse().code?.let {
                    setEditMode(true)
                }
            })
    }


    fun setEditMode(isEdit: Boolean) {
        _isProfileEdited.postValue(isEdit)
    }

    companion object {
        const val DUPLICATE_NICKNAME_CODE = 26
    }
}
