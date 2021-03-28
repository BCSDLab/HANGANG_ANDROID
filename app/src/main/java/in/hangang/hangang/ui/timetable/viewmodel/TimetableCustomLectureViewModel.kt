package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.source.TimeTableRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableCustomLectureViewModel(
    val timeTableRepository: TimeTableRepository
) : ViewModelBase() {
    private val times = mutableListOf<CustomTimetableTimestamp>()

    private val _timestamp = MutableLiveData<List<CustomTimetableTimestamp>>()
    private val _customTimetableAdded = MutableLiveData<Event<Boolean>>()

    val timestamp : LiveData<List<CustomTimetableTimestamp>> get() = _timestamp
    val customTimetableAdded :LiveData<Event<Boolean>> get() = _customTimetableAdded

    fun addTimestamp(customTimetableTimestamp: CustomTimetableTimestamp) {
        times.add(customTimetableTimestamp)
        _timestamp.postValue(times)
    }

    fun removeTimestamp(position: Int) {
        times.removeAt(position)
        _timestamp.postValue(times)
    }

    fun addCustomLecture(
        name: String,
        professor: String,
        classTime: String,
        userTimetableId: Int
    ) {
        timeTableRepository.addCustomLectureInTimetable(
            classTime, name, professor, userTimetableId
        )
    }
}