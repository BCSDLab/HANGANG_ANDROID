package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.SemesterUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableAddActivityViewModel(private val timeTableRepository: TimeTableRepository) : ViewModelBase() {
    private val _added = MutableLiveData(Event(false))
    private val _addingAvailable = MutableLiveData(false)
    private val _error = MutableLiveData<Event<CommonResponse>>()
    val added: LiveData<Event<Boolean>> get() = _added
    val addingAvailable : LiveData<Boolean> get() = _addingAvailable
    val error: LiveData<Event<CommonResponse>> get() = _error

    fun addTimeTable(year: Int = 2020, semester: Int, name: String) {
        timeTableRepository.makeTimeTable(UserTimeTableRequest(
                name = name,
                semesterDateId = SemesterUtil.getSemesterDateId(year, semester)))
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _added.postValue(Event(true))
                }, {
                    _error.postValue(Event(it.toCommonResponse()))
                })
    }

    fun checkAddingAvailability(name : String) {
        _addingAvailable.postValue(name.isNotEmpty() && name.length <= 20)
    }
}