package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.timetable.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.data.entity.timetable.TimeTable
import `in`.hangang.hangang.data.entity.timetable.toTimeTable
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.TimeTableRepository
import `in`.hangang.hangang.ui.customview.timetable.TimetableColumnHeader
import `in`.hangang.hangang.ui.customview.timetable.TimetableLayout
import `in`.hangang.hangang.ui.customview.timetable.TimetableUtil
import `in`.hangang.hangang.util.*
import `in`.hangang.hangang.util.file.FileUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class TimetableViewModel(
    private val timeTableRepository: TimeTableRepository
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_LECTURE_LIST,
        MODE_CUSTOM_LECTURE,
        MODE_LECTURE_DETAIL
    }

    private var showOnlyCustomLectureEventDialog = true

    private val listTimetables = mutableListOf<TimeTable>()

    private val times = mutableListOf<CustomTimetableTimestamp>()

    private val _timetables = MutableLiveData<Map<Int, List<TimeTable>>>()
    private val _mainTimetableEvent = MutableLiveData<Event<TimeTable>>()
    private val _setMainTimetableEvent = MutableLiveData<Event<CommonResponse>>()
    private val _timetableNameModifiedEvent = MutableLiveData<Event<String>>()
    private val _timetableBitmapSaved = MutableLiveData<Event<Boolean>>()
    private val _timetableBitmapError = MutableLiveData<Event<String>>()

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _displayingTimeTable = MutableLiveData<TimeTable>()
    private val _lectureTimetablesInTimetable = MutableLiveData<List<LectureTimeTable>>()
    private val _dummyTimeTable = MutableLiveData<LectureTimeTable?>()
    private val _onErrorAddLectureTimetable = MutableLiveData<Event<CommonResponse>>()
    private val _customLectureAdded = MutableLiveData<Event<Boolean>>()
    private val _availableAddingCustomTimetable = MutableLiveData<Boolean>()
    private val _lectureTimetableRemovedEvent = MutableLiveData<Event<Int>>()
    private val _bottomSheetCloseEvent = MutableLiveData<Event<Boolean>>()
    private val _onlyCustomLectureEvent = MutableLiveData<Event<Boolean>>()

    private val _timestamp = MutableLiveData<List<CustomTimetableTimestamp>>()

    private val _error = MutableLiveData<Event<CommonResponse>>()

    val timetables: LiveData<Map<Int, List<TimeTable>>> get() = _timetables
    val mainTimetableEvent: LiveData<Event<TimeTable>> get() = _mainTimetableEvent
    val setMainTimetableEvent: LiveData<Event<CommonResponse>> get() = _setMainTimetableEvent
    val timetableNameModifiedEvent: LiveData<Event<String>> get() = _timetableNameModifiedEvent
    val timetableBitmapSaved: LiveData<Event<Boolean>> get() = _timetableBitmapSaved
    val mode: LiveData<Mode> get() = _mode
    val displayingTimeTable: LiveData<TimeTable> get() = _displayingTimeTable
    val lectureTimetablesInTimetable: LiveData<List<LectureTimeTable>> get() = _lectureTimetablesInTimetable
    val dummyTimeTable: LiveData<LectureTimeTable?> get() = _dummyTimeTable
    val onErrorAddLectureTimetable: LiveData<Event<CommonResponse>> get() = _onErrorAddLectureTimetable
    val timestamp: LiveData<List<CustomTimetableTimestamp>> get() = _timestamp
    val customLectureAdded: LiveData<Event<Boolean>> get() = _customLectureAdded
    val availableAddingCustomTimetable: LiveData<Boolean> get() = _availableAddingCustomTimetable
    val lectureTimetableRemovedEvent: LiveData<Event<Int>> get() = _lectureTimetableRemovedEvent
    val bottomSheetCloseEvent: LiveData<Event<Boolean>> get() = _bottomSheetCloseEvent
    val timetableBitmapError : LiveData<Event<String>> get() = _timetableBitmapError
    val onlyCustomLectureEvent : LiveData<Event<Boolean>> get() = _onlyCustomLectureEvent

    val error: LiveData<Event<CommonResponse>> get() = _error

    fun setMode(mode: Mode) {
        if (_mode.value != mode) {
            if(mode == Mode.MODE_LECTURE_LIST && _displayingTimeTable.value?.semesterDateId?.isRegularSemester() == false) {
                _onlyCustomLectureEvent.postValue(Event(showOnlyCustomLectureEventDialog))
                _mode.postValue(Mode.MODE_CUSTOM_LECTURE)
                showOnlyCustomLectureEventDialog = false
            } else {
                _mode.postValue(mode)
            }
        }
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
                setDisplayingTimeTable(it.toTimeTable())
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                //TODO 시간표에 추가된 강의 아이템을 가져오지 못했을 때 에러메시지
                LogUtil.e(it.toCommonResponse().errorMessage)
                _error.value = Event(it.toCommonResponse())
            })
            .addTo(compositeDisposable)
    }

    fun getMainTimeTable() {
        timeTableRepository.getMainTimeTable()
            .withThread()
            .handleProgress(this)
            .handleHttpException()
            .subscribe({
                showOnlyCustomLectureEventDialog = true
                _mainTimetableEvent.postValue(Event(it.toTimeTable()))
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                _error.value = Event(it.toCommonResponse())
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
                _error.value = Event(it.toCommonResponse())
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
                setDisplayingTimeTable(it.toTimeTable())
                _lectureTimetablesInTimetable.postValue(it.lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                _error.value = Event(it.toCommonResponse())
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
                _error.value = Event(it.toCommonResponse())
                //TODO 시간표 이름 수정 중 오류
            })
            .addTo(compositeDisposable)
    }

    fun getTimetableBitmapImage(
        fileUtil: FileUtil,
        timetableColumnHeader: TimetableColumnHeader,
        timetableLayout: TimetableLayout
    ) {
        Completable.create { subscriber ->
                try {
                    val timetableLayoutBitmap = Bitmap.createBitmap(
                        timetableLayout.width,
                        timetableLayout.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val timetableHeaderBitmap = Bitmap.createBitmap(
                        timetableColumnHeader.width,
                        timetableColumnHeader.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val timetableLayoutCanvas = Canvas(timetableLayoutBitmap)
                    val timetableHeaderCanvas = Canvas(timetableHeaderBitmap)
                    timetableLayout.draw(timetableLayoutCanvas)
                    timetableColumnHeader.draw(timetableHeaderCanvas)

                    val bitmap = Bitmap.createScaledBitmap(
                        timetableHeaderBitmap,
                        timetableHeaderBitmap.width,
                        timetableHeaderBitmap.height + timetableLayoutBitmap.height,
                        true)
                    val canvas = Canvas(bitmap)
                    val paint = Paint().apply {
                        isDither = true
                        flags = Paint.ANTI_ALIAS_FLAG
                    }
                    canvas.drawBitmap(timetableHeaderBitmap, 0f, 0f, paint)
                    canvas.drawBitmap(
                        timetableLayoutBitmap,
                        0f,
                        timetableHeaderBitmap.height.toFloat(),
                        paint
                    )

                    timetableHeaderBitmap.recycle()
                    timetableLayoutBitmap.recycle()

                    fileUtil.saveImageToPictures(bitmap, "${displayingTimeTable.value?.name ?: "Unknown"}.jpg")

                    subscriber.onComplete()
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
        }
            .withThread()
            .handleProgress(this)
            .subscribe({
                _timetableBitmapSaved.value = Event(true)
            }, {
                LogUtil.e(it.localizedMessage)
                _timetableBitmapError.value = Event(it.localizedMessage ?: "")
            })
            .addTo(compositeDisposable)
    }

    fun setDisplayingTimeTable(timetable: TimeTable) {
        showOnlyCustomLectureEventDialog = true
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
                _error.value = Event(it.toCommonResponse())
            })
            .addTo(compositeDisposable)
    }

    fun addTimeTableLecture(
        timetable: TimeTable,
        lectureTimeTable: LectureTimeTable
    ) {
        timeTableRepository.addLectureInTimeTable(
            lectureId = lectureTimeTable.lectureTimetableId ?: 0,
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
                _onErrorAddLectureTimetable.postValue(Event(it.toCommonResponse()))
            })
            .addTo(compositeDisposable)
    }

    fun removeTimeTableLecture(
        timetable: TimeTable, lectureTimeTable: LectureTimeTable,
        closeBottomSheet: Boolean
    ) {
        timeTableRepository.removeLectureFromTimeTable(
            lectureId = lectureTimeTable.lectureTimetableId ?: 0,
            timetableId = timetable.id
        ).flatMap {
            timeTableRepository.getTimetable(timetable.id)
        }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lectureTimetableRemovedEvent.postValue(Event(lectureTimeTable.id))
                _lectureTimetablesInTimetable.postValue(it.lectureList)
                if (closeBottomSheet)
                    _bottomSheetCloseEvent.value = Event(true)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
                _error.value = Event(it.toCommonResponse())
                //TODO 시간표에 강의 삭제 중 에러 발생시
            })
            .addTo(compositeDisposable)
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
                _onErrorAddLectureTimetable.postValue(Event(it.toCommonResponse()))
                _error.value = Event(it.toCommonResponse())
            })
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