package `in`.hangang.hangang.ui.home.recentlectures.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecentLecturesFragmentViewModel : ViewModelBase() {
    private val _timetableCount = MutableLiveData(8)
    val timetableCount : LiveData<Int> get() = _timetableCount
}