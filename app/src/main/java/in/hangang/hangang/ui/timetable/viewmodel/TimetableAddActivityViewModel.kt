package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.constant.TIMETABLE_DEFAULT_SEMESTER_ID
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class TimetableAddActivityViewModel(private val timeTableRepository: TimeTableRepository) : ViewModelBase() {
    private val _isAdded = MutableLiveData(Event(false))
    private val _addingAvailable = MutableLiveData(false)
    private val _error = MutableLiveData<Event<CommonResponse>>()
    val isAdded: LiveData<Event<Boolean>> get() = _isAdded
    val addingAvailable: LiveData<Boolean> get() = _addingAvailable
    val error: LiveData<Event<CommonResponse>> get() = _error

    fun addTimeTable(semesterDateId: Int = TIMETABLE_DEFAULT_SEMESTER_ID, name: String) {
        timeTableRepository.makeTimeTable(UserTimeTableRequest(
                name = name,
                semesterDateId = semesterDateId
        ))
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _isAdded.postValue(Event(true))
                }, {
                    _error.postValue(Event(it.toCommonResponse()))
                })
                .addTo(compositeDisposable)
    }

    fun checkAddingAvailability(name: String) {
        _addingAvailable.postValue(name.isNotEmpty() && name.length <= 20)
    }
}