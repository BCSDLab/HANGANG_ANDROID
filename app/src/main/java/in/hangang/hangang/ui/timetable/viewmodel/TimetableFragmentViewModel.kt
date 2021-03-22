package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single

class TimetableFragmentViewModel(
) : ViewModelBase() {

    enum class Mode {
        MODE_NORMAL,
        MODE_EDIT
    }

    private val _mode = MutableLiveData(Mode.MODE_NORMAL)
    private val _currentShowingTimeTable = MutableLiveData<TimeTable>()
    private val _captured = MutableLiveData<Bitmap>()
    private val _captureError = MutableLiveData<Throwable>()

    val mode: LiveData<Mode> get() = _mode
    val currentShowingTimeTable: LiveData<TimeTable> get() = _currentShowingTimeTable
    val captured: LiveData<Bitmap> get() = _captured
    val captureError: LiveData<Throwable> get() = _captureError

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
}