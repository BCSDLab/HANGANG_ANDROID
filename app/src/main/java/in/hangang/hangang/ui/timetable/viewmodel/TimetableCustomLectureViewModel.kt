package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class TimetableCustomLectureViewModel(
    val timeTableRepository: TimeTableRepository
) : ViewModelBase() {
    private val times = mutableListOf<CustomTimetableTimestamp>()

    private val _timestamp = MutableLiveData<List<CustomTimetableTimestamp>>()
    private val _customTimetableAdded = MutableLiveData<Event<Boolean>>()
    private val _availableAddingCustomTimetable = MutableLiveData(false)

    val timestamp: LiveData<List<CustomTimetableTimestamp>> get() = _timestamp
    val customTimetableAdded: LiveData<Event<Boolean>> get() = _customTimetableAdded
    val availableAddingCustomTimetable: LiveData<Boolean> get() = _availableAddingCustomTimetable

    fun addTimestamp(customTimetableTimestamp: CustomTimetableTimestamp) {
        times.add(customTimetableTimestamp)
        _timestamp.postValue(times)
    }

    fun removeTimestamp(position: Int) {
        times.removeAt(position)
        _timestamp.postValue(times)
    }

    fun modifyTimestamp(position: Int, customTimetableTimestamp: CustomTimetableTimestamp) {
        times[position] = customTimetableTimestamp
        _timestamp.postValue(times)
    }

    fun checkValidation(name: String, professor: String) {
        _availableAddingCustomTimetable.postValue(
            name.isNotEmpty() &&
                    professor.isNotEmpty() &&
                    TimetableUtil.toExp(times) != "[]"
        )
    }

    fun addCustomLecture(
        name: String,
        professor: String,
        classTime: String,
        userTimetableId: Int
    ) {
        timeTableRepository.addCustomLectureInTimetable(
            classTime, name, professor, userTimetableId
        ).withThread()
            .handleProgress(this)
            .handleHttpException()
            .subscribe({
                _customTimetableAdded.postValue(Event(true))
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
                .addTo(compositeDisposable)
    }

    fun init() {
        times.clear()
        times.add(
            CustomTimetableTimestamp(
                week = 0,
                startTime = Pair(9, 0),
                endTime = Pair(10, 0)
            )
        )
        _timestamp.postValue(times)
    }
}