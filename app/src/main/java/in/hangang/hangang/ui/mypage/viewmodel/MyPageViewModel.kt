package `in`.hangang.hangang.ui.mypage.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.PointRecord
import `in`.hangang.hangang.data.entity.UploadFile
import `in`.hangang.hangang.data.entity.User
import `in`.hangang.hangang.data.entity.UserCount
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single

class MyPageViewModel(private val userRepository: UserRepository) : ViewModelBase() {

    private val pointRecordList = mutableListOf<PointRecord>()

    private val _user = MutableLiveData<User>()
    private val _userCount = MutableLiveData<UserCount>()
    private val _pointRecords = MutableLiveData<List<PointRecord>>()
    private val _uploadFiles = MutableLiveData<List<UploadFile>>()

    val user: LiveData<User> get() = _user
    val userCount: LiveData<UserCount> get() = _userCount
    val pointRecords: LiveData<List<PointRecord>> get() = _pointRecords
    val uploadFiles: LiveData<List<UploadFile>> get() = _uploadFiles

    fun getMyPageData() {
        Single.zip(
                userRepository.getUserInformation(),
                userRepository.getUserCounts()
        ) { user, userCount ->
            _user.postValue(user)
            _userCount.postValue(userCount)
        }
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({

                }, {

                })
    }

    fun getPointRecord() {
        userRepository.getPointRecords()
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    pointRecordList.clear()
                    pointRecordList.addAll(it)
                    _pointRecords.postValue(pointRecordList)
                }, {

                })
    }

    fun getUploadFiles() {
        userRepository.getPurchasedBanks()
                .map {
                    it.map { it.uploadFiles }.flatten()
                }
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _uploadFiles.postValue(it)
                }, {

                })
    }
}