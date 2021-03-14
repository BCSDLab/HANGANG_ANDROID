package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableListActivityViewModel : ViewModelBase() {

    private val _currentTimetableSize = MutableLiveData<Int>()

    val currentTimetableSize : LiveData<Int> get() = _currentTimetableSize

    fun updateTimetableSize(size : Int) {
        _currentTimetableSize.postValue(size)
    }
}