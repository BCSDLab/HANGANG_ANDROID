package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableLectureDetailViewModel(
        private val timeTableRepository: TimeTableRepository
) : ViewModelBase() {

    private val _lectureTimetable = MutableLiveData<LectureTimeTable>()
    private val _memo = MutableLiveData<String>()

    val lectureTimetable : LiveData<LectureTimeTable> get() = _lectureTimetable
    val memo : LiveData<String> get() = _memo

    fun setLectureTimetable(lectureTimeTable: LectureTimeTable) {
        _lectureTimetable.postValue(lectureTimeTable)
    }

}