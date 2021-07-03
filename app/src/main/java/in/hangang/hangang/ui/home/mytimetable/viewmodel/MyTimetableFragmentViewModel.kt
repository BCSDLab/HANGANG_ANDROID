package `in`.hangang.hangang.ui.home.mytimetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimeTableWithLecture
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class MyTimetableFragmentViewModel(private val timeTableRepository: TimeTableRepository, private val lectureRepository: LectureRepository) : ViewModelBase() {
    private val _timetableCount = MutableLiveData(0)
    val timetableCount: LiveData<Int> get() = _timetableCount
    private val _mainTimetable = MutableLiveData<TimeTableWithLecture>()
    val mainTimetable: LiveData<TimeTableWithLecture> get() = _mainTimetable

    private val _lecture = MutableLiveData<RankingLectureItem>()
    val lecture: LiveData<RankingLectureItem> get() = _lecture

    private val _mainTimetableList = MutableLiveData<List<LectureTimeTable>>()
    val mainTimetableList: LiveData<List<LectureTimeTable>> get() = _mainTimetableList

    fun getMainTimeTable() {
        timeTableRepository.getMainTimeTable()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _mainTimetable.value = it
                _mainTimetableList.value = it.lectureList
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }
    fun getLecturesId(id: Int) {
        lectureRepository.getLecturesId(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _lecture.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }
}