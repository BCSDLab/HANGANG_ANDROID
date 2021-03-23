package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single

class TimetableFragmentViewModel(
        private val timeTableRepository: TimeTableRepository
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_EDIT
    }

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _currentShowingTimeTable = MutableLiveData<TimeTable>()
    private val _captured = MutableLiveData<Bitmap>()
    private val _captureError = MutableLiveData<Throwable>()
    private val _lectureTimetableViews = MutableLiveData<List<View>>()
    private val _lectureTimetables = MutableLiveData<List<LectureTimeTable>>()

    val mode: LiveData<Mode> get() = _mode
    val currentShowingTimeTable: LiveData<TimeTable> get() = _currentShowingTimeTable
    val captured: LiveData<Bitmap> get() = _captured
    val captureError: LiveData<Throwable> get() = _captureError
    val lectureTimetableViews : LiveData<List<View>> get() = _lectureTimetableViews
    val lectureTimeTables : LiveData<List<LectureTimeTable>> get() = _lectureTimetables

    fun switchToEditMode() {
        if (_mode.value != Mode.MODE_EDIT)
            _mode.postValue(Mode.MODE_EDIT)
    }

    fun switchToNormalMode() {
        if (_mode.value != Mode.MODE_NORMAL)
            _mode.postValue(Mode.MODE_NORMAL)
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
                    _captureError.postValue(it)
                })
    }

    fun renderTimeTable(timetableUtil: TimetableUtil, timetable: TimeTable) {
        timeTableRepository.getLectureList(timetable.id).
        withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _lectureTimetables.postValue(it)
                    timetableUtil.getTimetableView(it)
                            .withThread()
                            .handleProgress(this)
                            .subscribe({ views ->
                                _lectureTimetableViews.postValue(views)
                            }, {
                                LogUtil.e(it.message)
                            })
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
    }
}