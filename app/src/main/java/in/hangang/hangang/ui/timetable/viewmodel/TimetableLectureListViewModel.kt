package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class TimetableLectureListViewModel(
        private val timetableRepository: TimeTableRepository
) : ViewModelBase() {
    private val lectureList = mutableListOf<LectureTimeTable>()
    private val scrapLectures = mutableListOf<LectureTimeTable>()

    private val _isShowingScraps = MutableLiveData<Boolean>()

    private val _lectures = MutableLiveData<Collection<LectureTimeTable>>()
    private val _scraps = MutableLiveData<Collection<LectureTimeTable>>()

    private val _timetableLectureChanged = MutableLiveData<Event<CommonResponse>>()

    private val _lectureFilter = MutableLiveData<LectureFilter>()
    private val _resetLectureFilter = MutableLiveData<Event<Boolean>>()

    val lectures: LiveData<Collection<LectureTimeTable>> get() = _lectures
    val timetableLectureChanged: MutableLiveData<Event<CommonResponse>> get() = _timetableLectureChanged
    val scraps: LiveData<Collection<LectureTimeTable>> get() = _scraps
    val isShowingScraps: LiveData<Boolean> get() = _isShowingScraps
    val lectureFilter: LiveData<LectureFilter> get() = _lectureFilter
    val resetLectureFilter: LiveData<Event<Boolean>> get() = _resetLectureFilter

    var page = 0
    var semesterDateId: Int = 5

    fun getLectures(
            semesterDateId: Int
    ) {
        setShowingScrap(false)
        page = 0
        this.semesterDateId = semesterDateId
        lectureList.clear()

        getLecturesRx()
                .handleProgress(this)
                .subscribe({
                    lectureList.addAll(it)
                    _lectures.postValue(lectureList)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
    }

    fun getLectures() {
        page++
        getLecturesRx()
                .subscribe({
                    lectureList.addAll(it)
                    _lectures.postValue(lectureList)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
                .addTo(compositeDisposable)
    }

    private fun getLecturesRx(): Single<List<LectureTimeTable>> {
        return timetableRepository.getLectureTimetableList(
                classification = lectureFilter.value?.classifications,
                criteria = lectureFilter.value?.criteria,
                department = lectureFilter.value?.department,
                keyword = lectureFilter.value?.keyword,
                page = page,
                semesterDateId = semesterDateId
        )
                .handleHttpException()
                .withThread()
    }

    fun toggleScrapLecture(lectureTimeTable: LectureTimeTable) {
        val scraps = scraps.value ?: setOf()
        if (scraps.contains(lectureTimeTable)) {
            unscrapLecture(lectureTimeTable)
        } else {
            scrapLecture(lectureTimeTable)
        }
    }

    private fun scrapLecture(lectureTimeTable: LectureTimeTable) {
        timetableRepository.scrapLecture(lectureTimeTable)
                .withThread()
                .subscribe({
                    scrapLectures.add(it)
                    _scraps.postValue(scrapLectures)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
    }

    private fun unscrapLecture(lectureTimeTable: LectureTimeTable) {
        timetableRepository.unscrapLecture(lectureTimeTable)
                .withThread()
                .subscribe({
                    scrapLectures.remove(it)
                    _scraps.postValue(scrapLectures)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
    }

    fun getScrapedLectures(
            switchLecturesList: Boolean
    ) {
        if (switchLecturesList) setShowingScrap(true)
        timetableRepository.getScrapLectures(
                _lectureFilter.value?.classifications,
                _lectureFilter.value?.department,
                _lectureFilter.value?.keyword)
                .withThread()
                .handleProgress(this)
                .subscribe({
                    scrapLectures.clear()
                    scrapLectures.addAll(it)
                    _scraps.postValue(scrapLectures)
                    if (switchLecturesList) _lectures.postValue(scrapLectures)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
    }

    fun setShowingScrap(value: Boolean) {
        _isShowingScraps.postValue(value)
    }

    fun setLectureFilter(lectureFilter: LectureFilter) {
        _lectureFilter.postValue(lectureFilter)
    }

    fun resetLectureFilter() {
        _resetLectureFilter.postValue(Event(true))
        _lectureFilter.postValue(
                LectureFilter()
        )
    }
}