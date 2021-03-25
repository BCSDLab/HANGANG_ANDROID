package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class TimetableLectureListViewModel(
        private val timetableRepository: TimeTableRepository
) : ViewModelBase() {
    private val lectureList = mutableListOf<LectureTimeTable>()

    private val _enableEndlessScroll = MutableLiveData<Boolean>()

    private val _lectures = MutableLiveData<Collection<LectureTimeTable>>()
    private val _dips = MutableLiveData<Collection<LectureTimeTable>>()

    private val _isGetLecturesLoading = MutableLiveData<Boolean>()
    private val _isGetLecturesAdditionalLoading = MutableLiveData<Boolean>()

    private val _timetableLectureChanged = MutableLiveData<Event<CommonResponse>>()

    val lectures: LiveData<Collection<LectureTimeTable>> get() = _lectures
    val isGetLecturesLoading: LiveData<Boolean> get() = _isGetLecturesLoading
    val timetableLectureChanged: MutableLiveData<Event<CommonResponse>> get() = _timetableLectureChanged
    val dips : LiveData<Collection<LectureTimeTable>> get() = _dips
    val enableEndlessScroll : LiveData<Boolean> get() = _enableEndlessScroll

    private var lastLecturesParameter: LecturesParameter = LecturesParameter()

    var page = 0
    var classification: List<String>? = null
    var department: String? = null
    var keyword: String? = null
    var semesterDateId: Int = 5

    fun getLectures(
            classification: List<String>? = null,
            department: String? = null,
            keyword: String? = null,
            semesterDateId: Int
    ) {
        _enableEndlessScroll.postValue(true)
        page = 0
        this.classification = classification
        this.department = department
        this.keyword = keyword
        this.semesterDateId = semesterDateId
        lectureList.clear()
        _lectures.postValue(lectureList)

        getLectures()
    }

    fun getLectures() {
        page++
        timetableRepository.getLectureTimetableList(
                classification = classification,
                department = department,
                keyword = keyword,
                page = page,
                semesterDateId = semesterDateId
        )
                .handleHttpException()
                .withThread()
                .doFinally {
                    _isGetLecturesAdditionalLoading.postValue(false)
                    _isGetLecturesLoading.postValue(false)
                }
                .subscribe({
                    lectureList.addAll(it)
                    _lectures.postValue(lectureList)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
                .addTo(compositeDisposable)
    }

    fun addTimeTableLecture(timetableId: Int, lectureId: Int) {
        timetableRepository.addLectureInTimeTable(
                lectureId = lectureId,
                timetableId = timetableId
        ).withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _timetableLectureChanged.postValue(Event(it))
                }, {
                    _timetableLectureChanged.postValue(Event(it.toCommonResponse()))
                })
                .addTo(compositeDisposable)
    }

    fun removeTimeTableLecture(timetableId: Int, lectureId: Int) {
        timetableRepository.removeLectureInTimeTable(
                lectureId = lectureId,
                timetableId = timetableId
        ).withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _timetableLectureChanged.postValue(Event(it))
                }, {
                    _timetableLectureChanged.postValue(Event(it.toCommonResponse()))
                })
                .addTo(compositeDisposable)
    }

    fun toggleDipLecture(lectureTimeTable: LectureTimeTable) {
        val dips = dips.value ?: setOf()
        if(dips.contains(lectureTimeTable)) {
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

    fun getDipLectures(switchLecturesList : Boolean) {
        if(switchLecturesList) _enableEndlessScroll.postValue(false)
        timetableRepository.getDipLectures()
                .withThread()
                .handleProgress(this)
                .subscribe({
                    _dips.postValue(it)
                    if(switchLecturesList) _lectures.postValue(it)
                }, {

                })
    }
}