package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.data.source.repository.UserRepository
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
    private val lectureBankRepository: LectureBankRepository,
    private val userRepository: UserRepository
) : ViewModelBase() {

    private var _userId = -1
    private var userScrapId = 0

    private val _lectureBankDetail = MutableLiveData<LectureBankDetail>()
    private val _isScraped = MutableLiveData<Boolean>()
    private val _isPurchased = MutableLiveData<Pair<Boolean, Int>>()
    private val _reportedEvent = MutableLiveData<Event<Boolean>>()
    private val _lectureBankCommentPagingData = MutableLiveData<PagingData<LectureBankComment>>()
    private val _errorEvent = MutableLiveData<Event<CommonResponse>>()
    private val _lectureBankCommentAppliedEvent = MutableLiveData<Event<Int>>()
    private val _lectureBankCommentRemovedEvent = MutableLiveData<Event<CommonResponse>>()
    private val _lectureBankCommentModifiedEvent = MutableLiveData<Event<CommonResponse>>()

    val lectureBankDetail : LiveData<LectureBankDetail> get() = _lectureBankDetail
    val isScraped : LiveData<Boolean> get() = _isScraped
    val isPurchased : LiveData<Pair<Boolean, Int>> get() = _isPurchased
    val reportedEvent : LiveData<Event<Boolean>> get() = _reportedEvent
    val lectureBankCommentPagingData : LiveData<PagingData<LectureBankComment>> get() = _lectureBankCommentPagingData
    val errorEvent : LiveData<Event<CommonResponse>> get() = _errorEvent
    val lectureBankCommentAppliedEvent : LiveData<Event<Int>> get() = _lectureBankCommentAppliedEvent
    val lectureBankCommentRemovedEvent : LiveData<Event<CommonResponse>> get() =  _lectureBankCommentRemovedEvent
    val lectureBankCommentModifiedEvent : LiveData<Event<CommonResponse>> get() =  _lectureBankCommentModifiedEvent
    val userId : Int get() = _userId

    fun getLectureBankDetail(id: Int) {
        userRepository.getUserInfo()
            .doOnSuccess {
                _userId = it.id
            }
            .flatMap {
                lectureBankRepository.getLectureBankDetail(id)
            }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _lectureBankDetail.value = it
                _isScraped.value = it.userScrapId > 0
                _isPurchased.value = it.isPurchased to it.pointPrice
                userScrapId = it.userScrapId
            }, this::postErrorViewModel)
            .addTo(compositeDisposable)
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

    fun modifyLectureBankComment(lectureBankComment: LectureBankComment, comment: String) {
        lectureBankRepository.modifyLectureBankComment(
            lectureBankId = lectureBankDetail.value?.id ?: -1,
            commentId = lectureBankComment.id ?: -1,
            comment = comment
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                 _lectureBankCommentModifiedEvent.value = Event(it)
            }, {

            })
            .addTo(compositeDisposable)
    }

    fun removeLectureBankComment(lectureBankComment: LectureBankComment) {
        lectureBankRepository.deleteLectureBankComment(
            lectureBankId = lectureBankDetail.value?.id ?: -1,
            commentId = lectureBankComment.id ?: -1
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lectureBankCommentRemovedEvent.value = Event(it)
            }, {

            })
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
            .addTo(compositeDisposable)
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
            .addTo(compositeDisposable)
    }

    fun purchaseLectureBank() {
        lectureBankRepository.purchaseLectureBank(lectureBankDetail.value?.id ?: -1)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _isPurchased.value = true to (lectureBankDetail.value?.pointPrice ?: 0)
            }, this::postErrorViewModel)
            .addTo(compositeDisposable)
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
            .addTo(compositeDisposable)
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
            .addTo(compositeDisposable)
    }

    fun commentLectureBank(comment: String) {
        lectureBankRepository.commentLectureBank(
            lectureBankDetail.value?.id ?: -1,
            comment
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe( {
                _lectureBankCommentAppliedEvent.value = Event(it)
            }, this::postErrorViewModel)
            .addTo(compositeDisposable)
    }

    private fun postErrorViewModel(t: Throwable) {
        _errorEvent.postValue(Event(t.toCommonResponse()))
    }
}