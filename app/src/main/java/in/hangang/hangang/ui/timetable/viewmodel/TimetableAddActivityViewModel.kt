package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.SemesterUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableAddActivityViewModel(private val timeTableRepository: TimeTableRepository) : ViewModelBase() {
    private val _added = MutableLiveData(false)
    private val _addingAvailable = MutableLiveData(false)
    val added: LiveData<Boolean> get() = _added
    val addingAvailable : LiveData<Boolean> get() = _addingAvailable

    fun addTimeTable(year: Int = 2020, semester: Int, name: String) {
        timeTableRepository.makeTimeTable(UserTimeTableRequest(
                name = name,
                semesterDateId = SemesterUtil.getSemesterDateId(year, semester)))
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    _added.postValue(true)
                }, {

                })
    }

    fun checkAddingAvailability(name : String) {
        _addingAvailable.postValue(name.isNotEmpty() && name.length <= 20)
    }
}