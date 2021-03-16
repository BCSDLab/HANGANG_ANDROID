package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.util.withThread
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableFragmentViewModel(
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_EDIT
    }

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _currentShowingTimeTable = MutableLiveData<TimeTable>()

    val mode : LiveData<Mode> get() = _mode
    val currentShowingTimeTable: LiveData<TimeTable> get() = _currentShowingTimeTable

    fun switchToEditMode() {
        if(_mode.value != Mode.MODE_EDIT)
        _mode.postValue(Mode.MODE_EDIT)
    }

    fun switchToNormalMode() {
        if(_mode.value != Mode.MODE_NORMAL)
        _mode.postValue(Mode.MODE_NORMAL)
    }

    fun setCurrentShowingTimeTable(timetable: TimeTable) {
        _currentShowingTimeTable.postValue(timetable)
    }
}