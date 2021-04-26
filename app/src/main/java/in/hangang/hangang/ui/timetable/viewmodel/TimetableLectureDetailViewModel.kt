package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class TimetableLectureDetailViewModel(
    private val timeTableRepository: TimeTableRepository
) : ViewModelBase() {

    private val _lectureTimetable = MutableLiveData<LectureTimeTable>()
    private val _memo = MutableLiveData<Event<String>>()

    val lectureTimetable: LiveData<LectureTimeTable> get() = _lectureTimetable
    val memo: LiveData<Event<String>> get() = _memo

    private fun setLectureTimetable(lectureTimeTable: LectureTimeTable) {
        _lectureTimetable.value = lectureTimeTable
    }

    fun initWithLectureTimetable(lectureTimeTable: LectureTimeTable) {
        setLectureTimetable(lectureTimeTable)
        getMemo(lectureTimeTable)
    }

    fun getMemo(lectureTimeTable: LectureTimeTable) {
        timeTableRepository.getMemo(lectureTimeTable.id)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _memo.postValue(Event(it.memo ?: ""))
            }, {
                _memo.postValue(Event(""))
            })
            .addTo(compositeDisposable)
    }

    fun updateMemo(memo: String) {
        _lectureTimetable.value?.id?.let { id ->
            (if (memo.isBlank()) {
                timeTableRepository.removeMemo(id)
            } else {
                timeTableRepository.modifyMemo(
                    timetableLectureId = id,
                    memo = memo
                )
            })
                .withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    getMemo(_lectureTimetable.value!!)
                }, {

                })
                .addTo(compositeDisposable)
        }
    }

}