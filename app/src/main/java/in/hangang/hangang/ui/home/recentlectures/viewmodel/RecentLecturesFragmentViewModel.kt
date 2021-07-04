package `in`.hangang.hangang.ui.home.recentlectures.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.source.repository.LectureRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecentLecturesFragmentViewModel(private val lectureRepository: LectureRepository) : ViewModelBase() {
    private val _timetableCount = MutableLiveData(0)
    val timetableCount: LiveData<Int> get() = _timetableCount

    private val _lectureList = MutableLiveData<ArrayList<RankingLectureItem>>()
    val lectureList: LiveData<ArrayList<RankingLectureItem>> get() = _lectureList

    fun getLectureList() {
        viewModelScope.launch {
            _lectureList.value = lectureRepository.getRecentlyLectureList()
        }
    }
}