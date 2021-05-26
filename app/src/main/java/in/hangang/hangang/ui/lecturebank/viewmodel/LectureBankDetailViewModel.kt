package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankDetailViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private var userScrapId = 0

    private val _lectureBankDetail = MutableLiveData<LectureBankDetail>()
    private val _isScraped = MutableLiveData<Boolean>()
    private val _reportedEvent = MutableLiveData<Event<Boolean>>()
    private val _lectureBankCommentPagingData = MutableLiveData<PagingData<LectureBankComment>>()
    private val _errorEvent = MutableLiveData<Event<CommonResponse>>()

    val lectureBankDetail : LiveData<LectureBankDetail> get() = _lectureBankDetail
    val isScraped : LiveData<Boolean> get() = _isScraped
    val reportedEvent : LiveData<Event<Boolean>> get() = _reportedEvent
    val lectureBankCommentPagingData : LiveData<PagingData<LectureBankComment>> get() = _lectureBankCommentPagingData
    val errorEvent : LiveData<Event<CommonResponse>> get() = _errorEvent

    fun getLectureBankDetail(id: Int) {
        lectureBankRepository.getLectureBankDetail(id)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _lectureBankDetail.value = it
                _isScraped.value = it.userScrapId > 0
                userScrapId = it.userScrapId
            }, this::postErrorViewModel)
    }

    fun getLectureBankComments() {
        lectureBankRepository.getLectureBankComments(
            lectureBankDetail.value?.id ?: -1
        )
            .cachedIn(viewModelScope)
            .subscribe {
                _lectureBankCommentPagingData.value = it
            }
            .addTo(compositeDisposable)
    }

    fun scrapLecture(lectureId: Int) {
        lectureBankRepository.scrapLectureBank(lectureId)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                userScrapId = it
                _isScraped.value = true
            }, this::postErrorViewModel)
    }

    fun unscrapLecture() {
        if(userScrapId > 0)
        lectureBankRepository.unscrapLectureBank(userScrapId)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _isScraped.value = false
            }, this::postErrorViewModel)
    }

    fun reportLecture(type: Int) {
        lectureBankRepository.reportLectureBank(
            lectureBankId = lectureBankDetail.value?.id ?: -1,
            reportId = type
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _reportedEvent.value = Event(true)
            }, this::postErrorViewModel)
    }

    fun reportComment(commentId: Int, type: Int) {
        lectureBankRepository.reportLectureBankComment(
            commentId = commentId,
            reportId = type
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _reportedEvent.value = Event(true)
            }, this::postErrorViewModel)
    }

    private fun postErrorViewModel(t: Throwable) {
        _errorEvent.postValue(Event(t.toCommonResponse()))
    }
}