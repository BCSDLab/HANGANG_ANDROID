package `in`.hangang.hangang.ui.mypage.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyScrapLectureBankViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val _scraps = MutableLiveData<List<LectureBankScrap>>()
    private val _isEditMode = MutableLiveData(false)
    private val _canRemoveLectureBank = MutableLiveData(false)

    val scraps : LiveData<List<LectureBankScrap>> get() = _scraps
    val isEditMode: LiveData<Boolean> get() = _isEditMode
    val canRemoveLectureBank: LiveData<Boolean> get() = _canRemoveLectureBank

    fun getLectureBankScraps() {
        lectureBankRepository.getScrapLectureBanks()
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                       _scraps.postValue(it)
            }, {

            })
    }

    fun unscrapLecture(vararg lectureBankScrap: LectureBankScrap) {
        lectureBankRepository.unscrapLectureBanks(lectureBankScrap.map { it.scrapId }.toList())
            .flatMap { lectureBankRepository.getScrapLectureBanks() }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _isEditMode.value = false
                _scraps.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.postValue(isEdit)
    }

    fun changeRemoveButtonState(isEnabled: Boolean) {
        _canRemoveLectureBank.postValue(isEnabled)
    }
}