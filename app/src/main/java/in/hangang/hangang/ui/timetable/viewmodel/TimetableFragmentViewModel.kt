package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
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
    val mode : LiveData<Mode> get() = _mode

    fun switchToEditMode() {
        if(_mode.value != Mode.MODE_EDIT)
        _mode.postValue(Mode.MODE_EDIT)
    }

    fun switchToNormalMode() {
        if(_mode.value != Mode.MODE_NORMAL)
        _mode.postValue(Mode.MODE_NORMAL)
    }
}