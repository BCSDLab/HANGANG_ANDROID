package `in`.hangang.hangang.ui.mypage.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyScrapLectureReviewViewModel(
    private val lectureRepository: LectureRepository
) : ViewModelBase() {

    private val _myScrapLecture = MutableLiveData<List<RankingLectureItem>>()
    private val _isEditMode = MutableLiveData(false)
    private val _canRemoveLecture = MutableLiveData(false)

    val myScrapLecture: LiveData<List<RankingLectureItem>> get() = _myScrapLecture
    val isEditMode: LiveData<Boolean> get() = _isEditMode
    val canRemoveLecture: LiveData<Boolean> get() = _canRemoveLecture

    fun getMyScrapLecture() {
        lectureRepository.getScrapedLecture()
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _myScrapLecture.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun unscrapLecture(vararg lectures: RankingLectureItem) {
        lectureRepository.unscrapLecture(*lectures.map { it.id }.toIntArray())
            .flatMap { lectureRepository.getScrapedLecture() }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _isEditMode.value = false
                _myScrapLecture.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.postValue(isEdit)
    }

    fun changeRemoveButtonState(isEnabled: Boolean) {
        _canRemoveLecture.postValue(isEnabled)
    }
}