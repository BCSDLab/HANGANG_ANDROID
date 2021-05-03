package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.toTimeTable
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.ui.customview.timetable.TimetableUtil
import `in`.hangang.hangang.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import java.io.EOFException

class TimetableViewModel(
        private val timeTableRepository: TimeTableRepository
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_LECTURE_LIST,
        MODE_CUSTOM_LECTURE,
        MODE_LECTURE_DETAIL
    }

    private val listTimetables = mutableListOf<TimeTable>()

    private val times = mutableListOf<CustomTimetableTimestamp>()

    private val _timetables = MutableLiveData<Map<Int, List<TimeTable>>>()
    private val _mainTimetableEvent = MutableLiveData<Event<TimeTable>>()
    private val _setMainTimetableEvent = MutableLiveData<Event<CommonResponse>>()
    private val _timetableNameModifiedEvent = MutableLiveData<Event<String>>()
    private val _timetableBitmapImage = MutableLiveData<Bitmap>()

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _displayingTimeTable = MutableLiveData<TimeTable>()
    private val _lectureTimetablesInTimetable = MutableLiveData<List<LectureTimeTable>>()
    private val _selectedTimetable = MutableLiveData<LectureTimeTable?>()
    private val _dummyTimeTable = MutableLiveData<LectureTimeTable?>()
    private val _duplicatedLectureTimetable = MutableLiveData<Event<LectureTimeTable?>>()
    private val _customLectureAdded = MutableLiveData<Event<Boolean>>()
    private val _availableAddingCustomTimetable = MutableLiveData<Boolean>()

    private val _timestamp = MutableLiveData<List<CustomTimetableTimestamp>>()

    val timetables: LiveData<Map<Int, List<TimeTable>>> get() = _timetables
    val mainTimetableEvent: LiveData<Event<TimeTable>> get() = _mainTimetableEvent
    val setMainTimetableEvent: LiveData<Event<CommonResponse>> get() = _setMainTimetableEvent
    val timetableNameModifiedEvent: LiveData<Event<String>> get() = _timetableNameModifiedEvent
    val timetableBitmapImage: LiveData<Bitmap> get() = _timetableBitmapImage
    val mode: LiveData<Mode> get() = _mode
    val displayingTimeTable: LiveData<TimeTable> get() = _displayingTimeTable
    val lectureTimetablesInTimetable: LiveData<List<LectureTimeTable>> get() = _lectureTimetablesInTimetable
    val selectedTimetable: LiveData<LectureTimeTable?> get() = _selectedTimetable
    val dummyTimeTable: LiveData<LectureTimeTable?> get() = _dummyTimeTable
    val duplicatedLectureTimetable: LiveData<Event<LectureTimeTable?>> get() = _duplicatedLectureTimetable
    val timestamp: LiveData<List<CustomTimetableTimestamp>> get() = _timestamp
    val customLectureAdded: LiveData<Event<Boolean>> get() = _customLectureAdded
    val availableAddingCustomTimetable: LiveData<Boolean> get() = _availableAddingCustomTimetable

    fun setMode(mode: Mode) {
        if (_mode.value != mode)
            _mode.postValue(mode)
    }

    fun getTimetables() {
        timeTableRepository.getTimeTables()
                .withThread()
                .handleProgress(this)
                .handleHttpException()
                .doOnSuccess {
                    listTimetables.clear()
                    listTimetables.addAll(it.toValuesList())
                }
                .subscribe({
                    _timetables.postValue(it)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                    // TODO 시간표 목록 가져오기 중 오류
                })
    }

    fun getTimeTable(timetable: TimeTable) {
        timeTableRepository.getTimetable(timetable.id)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _displayingTimeTable.postValue(it.toTimeTable())
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                //TODO 시간표에 추가된 강의 아이템을 가져오지 못했을 때 에러메시지
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }

    fun getMainTimeTable() {
        timeTableRepository.getMainTimeTable()
            .withThread()
            .handleProgress(this)
            .handleHttpException()
            .subscribe({
                _mainTimetableEvent.postValue(Event(it.toTimeTable()))
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                //TODO 메인 시간표 가져오는 중 오류
            })
    }

    fun setMainTimetable(timetableId: Int) {
        timeTableRepository.setMainTimeTable(timetableId)
                .withThread()
                .handleProgress(this)
                .handleHttpException()
                .subscribe({
                    _setMainTimetableEvent.value = Event(it)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                    //TODO 메인 시간표 설정 중 오류
                })
    }

    fun removeTimetable(timetable: TimeTable) {
        timeTableRepository.removeTimeTable(timetableId = timetable.id)
            .flatMap {
                getTimetablesRx()
            }.flatMap {
                timeTableRepository.getMainTimeTable()
            }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _displayingTimeTable.postValue(it.toTimeTable())
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                //TODO 시간표 삭제 중 오류
            })
    }

    fun modifyTimeTableName(timetable: TimeTable, name: String) {
        timeTableRepository.modifyTimeTableName(timetable.id, name)
                .withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _timetableNameModifiedEvent.postValue(Event(name))
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                    //TODO 시간표 이름 수정 중 오류
                })
                .addTo(compositeDisposable)
    }

    fun getTimetableBitmapImage(viewGroup: ViewGroup) {
        Single.create<Bitmap> { subscriber ->
            try {
                val bitmap = Bitmap.createBitmap(
                        viewGroup.measuredWidth,
                        viewGroup.measuredHeight,
                        Bitmap.Config.ARGB_8888
                )
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
                    _timetableBitmapImage.postValue(it)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                    //TODO 시간표 이미지 만드는 중 오류
                })
                .addTo(compositeDisposable)
    }

    fun setDisplayingTimeTable(timetable: TimeTable) {
        _displayingTimeTable.value = timetable
    }

    fun getLectureTimeTablesInTimeTable(timetable: TimeTable) {
        timeTableRepository.getTimetable(timetable.id)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                //TODO 시간표에 추가된 강의 아이템을 가져오지 못했을 때 에러메시지
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }

    private fun checkLectureDuplication(classTime: String): LectureTimeTable? {
        _lectureTimetablesInTimetable.value?.forEach {
            if (TimetableUtil.isLectureTimetableTimeDuplicate(it.classTime ?: "[]", classTime)) {
                _duplicatedLectureTimetable.postValue(Event(it))
                return it
            }
        }
        _duplicatedLectureTimetable.postValue(Event(null))
        return null
    }

    fun addTimeTableLecture(timetable: TimeTable, lectureTimeTable: LectureTimeTable) {
        if (checkLectureDuplication(lectureTimeTable.classTime ?: "[]") == null) {
            timeTableRepository.addLectureInTimeTable(
                    lectureId = lectureTimeTable.id,
                    timetableId = timetable.id
            ).flatMap {
                timeTableRepository.getTimetable(timetable.id)
            }
                    .withThread()
                    .handleHttpException()
                    .handleProgress(this)
                    .subscribe({
                        _lectureTimetablesInTimetable.postValue(it.lectureList)
                    }, {
                        LogUtil.e(it.toCommonResponse().errorMessage)
                        //TODO 시간표에 강의 추가 중 에러 발생시
                    })
                    .addTo(compositeDisposable)
        }
    }

    fun removeTimeTableLecture(timetable: TimeTable, lectureTimeTable: LectureTimeTable) {
        timeTableRepository.removeLectureFromTimeTable(
            lectureId = lectureTimeTable.lectureId,
            timetableId = timetable.id
        ).flatMap {
            timeTableRepository.getTimetable(timetable.id)
        }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                //TODO 시간표에 강의 삭제 중 에러 발생시
            })
            .addTo(compositeDisposable)
    }

    fun selectLectureTimeTable(lectureTimeTable: LectureTimeTable?) {
        _selectedTimetable.postValue(lectureTimeTable)
    }

    fun setDummyLectureTimeTable(lectureTimeTable: LectureTimeTable?) {
        _dummyTimeTable.postValue(lectureTimeTable)
    }

    fun addTimestamp(customTimetableTimestamp: CustomTimetableTimestamp) {
        times.add(customTimetableTimestamp)
        _timestamp.postValue(times)
    }

    fun removeTimestamp(position: Int) {
        times.removeAt(position)
        _timestamp.postValue(times)
    }

    fun modifyTimestamp(position: Int, customTimetableTimestamp: CustomTimetableTimestamp) {
        times[position] = customTimetableTimestamp
        _timestamp.postValue(times)
    }

    fun checkValidation(name: String, professor: String) {
        _availableAddingCustomTimetable.postValue(
                name.isNotEmpty() &&
                        professor.isNotEmpty() &&
                        TimetableUtil.convertCustomTimetableTimestampToApiExpression(times) != "[]"
        )
    }

    fun addCustomLecture(
            name: String,
            professor: String,
            classTime: String,
            timetableId: Int
    ) {
        if (checkLectureDuplication(classTime) == null) {
            timeTableRepository.addCustomLectureInTimetable(
                    classTime, name, professor, timetableId
            )
                .flatMap {
                    timeTableRepository.getTimetable(timetableId)
                }
                .withThread()
                .handleProgress(this)
                .handleHttpException()
                .subscribe({
                    _customLectureAdded.postValue(Event(true))
                    _lectureTimetablesInTimetable.postValue(it.lectureList)
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
        }

    }

    fun initCustomLectureValue() {
        times.clear()
        times.add(
                CustomTimetableTimestamp(
                        week = 0,
                        startTime = Pair(9, 0),
                        endTime = Pair(10, 0)
                )
        )
        _timestamp.postValue(times)
    }

    private fun getTimetablesRx(): Single<Map<Int, List<TimeTable>>> {
        return timeTableRepository.getTimeTables().doOnSuccess {
            listTimetables.clear()
            listTimetables.addAll(it.toValuesList())
            _timetables.postValue(it)
        }
    }

}