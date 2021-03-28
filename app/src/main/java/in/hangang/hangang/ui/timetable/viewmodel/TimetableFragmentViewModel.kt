package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class TimetableFragmentViewModel(
        private val timetableRepository: TimeTableRepository
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_LECTURE_LIST,
        MODE_CUSTOM_LECTURE,
        MODE_LECTURE_DETAIL
    }

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _currentShowingTimeTable = MutableLiveData<TimeTable>()
    private val _captured = MutableLiveData<Bitmap>()
    private val _lectureTimetables = MutableLiveData<List<LectureTimeTable>>()
    private val _selectedTimetable = MutableLiveData<LectureTimeTable?>()
    private val _dummyTimeTable = MutableLiveData<LectureTimeTable?>()

    val mode: LiveData<Mode> get() = _mode
    val currentShowingTimeTable: LiveData<TimeTable> get() = _currentShowingTimeTable
    val captured: LiveData<Bitmap> get() = _captured
    val lectureTimeTables : LiveData<List<LectureTimeTable>> get() = _lectureTimetables
    val selectedTimeTable : LiveData<LectureTimeTable?> get() = _selectedTimetable
    val dummyTimeTable : LiveData<LectureTimeTable?> get() = _dummyTimeTable

    fun setMode(mode: Mode) {
        if (_mode.value != mode)
            _mode.postValue(mode)
    }

    fun setCurrentShowingTimeTable(timetable: TimeTable) {
        _currentShowingTimeTable.postValue(timetable)
    }

    fun saveToBitmap(viewGroup: ViewGroup) {
        Single.create<Bitmap> { subscriber ->
            try {
                val bitmap = Bitmap.createBitmap(viewGroup.measuredWidth, viewGroup.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                viewGroup.draw(canvas)
                subscriber.onSuccess(bitmap)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
                .withThread()
                .handleProgress(this)
                .subscribe({
                           _captured.postValue(it)
                }, {
                })
                .addTo(compositeDisposable)
    }

    fun getAddedLectureTimeTables(timetable: TimeTable) {
        timetableRepository.getLectureList(timetable.id).
        withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _lectureTimetables.postValue(it)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
                .addTo(compositeDisposable)
    }

    fun checkLectureDuplication(lectureTimeTable: LectureTimeTable): LectureTimeTable? {
        _lectureTimetables.value?.forEach {
            if (TimetableUtil.isLectureTimetableTimeDuplicate(it, lectureTimeTable))
                return it
        }
        return null
    }

    fun addTimeTableLecture(timetable: TimeTable, lectureTimeTable: LectureTimeTable) {
        timetableRepository.addLectureInTimeTable(
                lectureId = lectureTimeTable.id,
                timetableId = timetable.id
        ).withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    getAddedLectureTimeTables(timetable)
                }, {

                })
                .addTo(compositeDisposable)
    }

    fun removeTimeTableLecture(timetable: TimeTable, lectureTimeTable: LectureTimeTable) {
        timetableRepository.removeLectureFromTimeTable(
                lectureId = lectureTimeTable.lectureId,
                timetableId = timetable.id
        ).withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    getAddedLectureTimeTables(timetable)
                }, {

                })
                .addTo(compositeDisposable)
    }

    fun selectLectureTimeTable(lectureTimeTable: LectureTimeTable?) {
        _selectedTimetable.postValue(lectureTimeTable)
    }

    fun setDummyLectureTimeTable(lectureTimeTable: LectureTimeTable?) {
        _dummyTimeTable.postValue(lectureTimeTable)
    }

}