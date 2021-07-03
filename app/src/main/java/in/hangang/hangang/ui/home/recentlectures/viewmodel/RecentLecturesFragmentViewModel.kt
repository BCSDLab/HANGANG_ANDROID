package `in`.hangang.hangang.ui.home.recentlectures.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.constant.RECENTLY_READ_LECTURE_REVIEW
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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