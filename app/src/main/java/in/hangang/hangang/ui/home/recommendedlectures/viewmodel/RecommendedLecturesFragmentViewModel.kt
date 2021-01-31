package `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecommendedLecturesFragmentViewModel : ViewModelBase() {
    private val _timetableCount = MutableLiveData(0)
    val timetableCount: LiveData<Int> get() = _timetableCount
}