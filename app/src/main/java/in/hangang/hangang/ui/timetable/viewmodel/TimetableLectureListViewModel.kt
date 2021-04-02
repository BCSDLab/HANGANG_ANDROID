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

    private val _isShowingDip = MutableLiveData<Boolean>()

    private val _lectures = MutableLiveData<Collection<LectureTimeTable>>()
    private val _dips = MutableLiveData<Collection<LectureTimeTable>>()

    private val _timetableLectureChanged = MutableLiveData<Event<CommonResponse>>()

    private val _lectureFilter = MutableLiveData<LectureFilter>()
    private val _resetLectureFilter = MutableLiveData<Event<Boolean>>()

    val lectures: LiveData<Collection<LectureTimeTable>> get() = _lectures
    val timetableLectureChanged: MutableLiveData<Event<CommonResponse>> get() = _timetableLectureChanged
    val dips: LiveData<Collection<LectureTimeTable>> get() = _dips
    val isShowingDip: LiveData<Boolean> get() = _isShowingDip
    val lectureFilter: LiveData<LectureFilter> get() = _lectureFilter
    val resetLectureFilter: LiveData<Event<Boolean>> get() = _resetLectureFilter

    var page = 0
    var semesterDateId: Int = 5

    fun getLectures(
            semesterDateId: Int
    ) {
        setShowingDip(false)
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
                department = lectureFilter.value?.department,
                keyword = lectureFilter.value?.keyword,
                page = page,
                semesterDateId = semesterDateId
        )
                .handleHttpException()
                .withThread()
    }

    fun toggleDipLecture(lectureTimeTable: LectureTimeTable) {
        val dips = dips.value ?: setOf()
        if (dips.contains(lectureTimeTable)) {
            removeDipLecture(lectureTimeTable)
        } else {
            addDipLecture(lectureTimeTable)
        }
    }

    private fun addDipLecture(lectureTimeTable: LectureTimeTable) {
        timetableRepository.addDipLecture(lectureTimeTable)
                .withThread()
                .handleProgress(this)
                .subscribe({
                    getDipLectures(false)
                }, {

                })
    }

    private fun removeDipLecture(lectureTimeTable: LectureTimeTable) {
        timetableRepository.removeDipLecture(lectureTimeTable)
                .withThread()
                .handleProgress(this)
                .subscribe({
                    getDipLectures(false)
                }, {

                })
    }

    fun getDipLectures(
            switchLecturesList: Boolean,
            classification: List<String>? = null,
            department: String? = null,
            keyword: String? = null
    ) {
        if (switchLecturesList) setShowingDip(true)
        timetableRepository.getDipLectures(classification, department, keyword)
                .withThread()
                .handleProgress(this)
                .subscribe({
                    _dips.postValue(it)
                    if (switchLecturesList) _lectures.postValue(it)
                }, {

                })
    }

    fun setShowingDip(value: Boolean) {
        _isShowingDip.postValue(value)
    }

    fun setLectureFilter(lectureFilter: LectureFilter) {
        _lectureFilter.postValue(lectureFilter)
    }

    fun resetLectureFilter() {
        _resetLectureFilter.postValue(Event(true))
        _lectureFilter.postValue(
                LectureFilter(
                        classifications = listOf(),
                        department = null,
                        criteria = LectureFilter.CRITERIA_NAME_PROFESSOR,
                        keyword = null
                )
        )
    }
}